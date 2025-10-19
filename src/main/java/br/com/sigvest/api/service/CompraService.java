package br.com.sigvest.api.service;

import br.com.sigvest.api.model.compras.Compra;
import br.com.sigvest.api.model.compras.EstornoCompras;
import br.com.sigvest.api.model.compras.ItemCompras;
import br.com.sigvest.api.model.compras.StatusCompra;
import br.com.sigvest.api.model.compras.dto.EstornoCompraRequest;
import br.com.sigvest.api.model.financeiro.MovimentarFinanceiro;
import br.com.sigvest.api.model.financeiro.StatusFinanceiro;
import br.com.sigvest.api.model.financeiro.TipoFinanceiro;
import br.com.sigvest.api.model.produto.Derivacao;
import br.com.sigvest.api.repository.Compras.CompraRepository;
import br.com.sigvest.api.repository.Compras.EstornoComprasRepository;
import br.com.sigvest.api.repository.Compras.ItemComprasRepository;
import br.com.sigvest.api.repository.DerivacaoRepository;
import br.com.sigvest.api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompraService {

    private final CompraRepository compraRepository;
    private final ItemComprasRepository itemComprasRepository;
    private final ProdutoRepository produtoRepository;
    private final DerivacaoRepository derivacaoRepository;
    private final MovimentarFinanService movimentarFinanService;
    private final EstornoComprasRepository estornoComprasRepository;

    @Transactional
    public Compra processarCompraCompleta(Compra compra) {
        validarCompra(compra); // já existente
        // vincular itens ao pai e normalizar referências
        if (compra.getItens() != null) {
            for (ItemCompras item : compra.getItens()) {
                item.setCompra(compra); // ESSENCIAL para gravar id_compra
                if (item.getDerivacao() != null && item.getDerivacao().getIdDerivacao() != null) {
                    var ref = derivacaoRepository.getReferenceById(item.getDerivacao().getIdDerivacao());
                    item.setDerivacao(ref);
                }
            }
        }
        MovimentarFinanceiro mov = criarMovimentacaoFinanceira(compra);
        compra.setMovimentarFinanceiro(mov);
        mov.setCompra(compra);
        Compra compraSalva = compraRepository.save(compra); // Cascade ALL em Compra.itens
        for (ItemCompras item : compraSalva.getItens()) { // mantém sua lógica de preço/estoque
            processarItemCompra(item);
        }
        return compraSalva;
    }


    @Transactional
    public void processarItemCompra(ItemCompras item) {
        // Buscar e validar derivação
        Derivacao derivacaoValidada = derivacaoRepository.findById(item.getDerivacao().getIdDerivacao())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Derivação não encontrada: " + item.getDerivacao().getIdDerivacao()));

        item.setDerivacao(derivacaoValidada);

        // Calcular valor líquido
        calcularValorLiquido(item);

        // Atualizar estoque e preços da derivação
        atualizarDerivacao(derivacaoValidada, item);

        log.info("Item processado - Produto: {}, Derivação: {}, Quantidade: {}, Preço Custo: {}, Preço Venda: {}",
                derivacaoValidada.getProduto().getNomeProduto(),
                derivacaoValidada.getCodigoSKU(),
                item.getQuantidade(),
                item.getValorBruto(),
                calcularPrecoVenda(item.getValorBruto(), item.getMargem()));
    }

    /**
     * Atualiza o estoque e os preços da derivação com base no item comprado.
     * - Estoque: adiciona a quantidade comprada
     * - Preço Custo: valor bruto por unidade
     * - Preço Venda: valor bruto + margem
     * - Margem Venda: percentual de margem
     */
    private void atualizarDerivacao(Derivacao derivacao, ItemCompras item) {
        // Atualizar estoque
        int quantidadeComprada = item.getQuantidade().intValue();
        Integer estoqueAtual = derivacao.getEstoque() != null ? derivacao.getEstoque() : 0;
        Integer novoEstoque = estoqueAtual + quantidadeComprada;
        derivacao.setEstoque(novoEstoque);

        // Atualizar preços
        BigDecimal valorBruto = item.getValorBruto(); // Preço custo por unidade
        BigDecimal margemPercent = item.getMargem() != null ? item.getMargem() : BigDecimal.ZERO;

        // Preço custo = valor bruto unitário
        derivacao.setPrecoCusto(valorBruto);

        // Preço venda = preço custo + margem
        BigDecimal precoVenda = calcularPrecoVenda(valorBruto, margemPercent);
        derivacao.setPrecoVenda(precoVenda);

        // Margem de venda em percentual
        derivacao.setMargemVenda(margemPercent);

        // Salvar derivação atualizada
        derivacaoRepository.save(derivacao);

        log.info("Derivação atualizada - SKU: {}, Estoque: {} → {}, Custo: {}, Venda: {}, Margem: {}%",
                derivacao.getCodigoSKU(), estoqueAtual, novoEstoque,
                valorBruto, precoVenda, margemPercent);
    }

    /**
     * Calcula o preço de venda com base no preço custo e margem percentual.
     * Preço Venda = Preço Custo * (1 + Margem/100)
     */
    private BigDecimal calcularPrecoVenda(BigDecimal precoCusto, BigDecimal margemPercent) {
        if (precoCusto == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal margem = margemPercent != null ? margemPercent : BigDecimal.ZERO;
        BigDecimal fatorMargem = BigDecimal.ONE.add(margem.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        return precoCusto.multiply(fatorMargem).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula o valor líquido do item (quantidade * valorBruto) + margem.
     * Valida e define o valor calculado.
     */
    private void calcularValorLiquido(ItemCompras item) {
        BigDecimal quantidade = item.getQuantidade();
        BigDecimal valorBruto = item.getValorBruto();
        BigDecimal margemPercent = item.getMargem() != null ? item.getMargem() : BigDecimal.ZERO;

        BigDecimal subtotal = quantidade.multiply(valorBruto);
        BigDecimal acrescimo = subtotal.multiply(margemPercent.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        BigDecimal valorLiquidoCalculado = subtotal.add(acrescimo).setScale(2, RoundingMode.HALF_UP);

        // Validar se o valor enviado está correto (tolerância de 0.02 por arredondamento)
        if (item.getValorLiquido() != null) {
            BigDecimal diferenca = item.getValorLiquido().subtract(valorLiquidoCalculado).abs();
            if (diferenca.compareTo(new BigDecimal("0.02")) > 0) {
                log.warn("Divergência no valor líquido - Esperado: {}, Recebido: {}",
                        valorLiquidoCalculado, item.getValorLiquido());
            }
        }

        // Define o valor calculado
        item.setValorLiquido(valorLiquidoCalculado);
    }

    /**
     * Cria a movimentação financeira a partir dos dados da compra.
     * Conversão simplificada de datas.
     */
    private MovimentarFinanceiro criarMovimentacaoFinanceira(Compra compra) {
        MovimentarFinanceiro mov = compra.getMovimentarFinanceiro();

        // Configurar valores calculados
        mov.setValor(compra.getTotalCompra());

        // Converter datas de forma simplificada
        mov.setDataLancamento(converterLocalDateParaDate(compra.getDataCompra()));

        // Configurar tipos e status padrão
        mov.setTipoFinanceiro(TipoFinanceiro.DESPESA);
        mov.setStatus(StatusFinanceiro.PENDENTE);

        // Criar descrição automática
        String descricao = String.format("Compra - Nota: %s - Fornecedor: %s",
                compra.getNumeroNota(),
                compra.getFornecedor() != null ? compra.getFornecedor().getRazaoSocial() : "N/A");
        mov.setDescricao(descricao);

        return mov;
    }

    /**
     * Converte LocalDate para Date usando meio-dia UTC para evitar problemas de fuso horário.
     */
    private Date converterLocalDateParaDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atTime(12, 0).atZone(ZoneId.of("UTC")).toInstant());
    }

    // ========== MÉTODOS DE CONSULTA ==========

    @Transactional(readOnly = true)
    public List<Compra> buscarComprasPorPeriodo(LocalDate inicio, LocalDate fim) {
        return compraRepository.findComprasComDetalhesPorPeriodo(inicio, fim);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalComprasPorPeriodo(LocalDate inicio, LocalDate fim) {
        return compraRepository.findTotalComprasPorPeriodo(inicio, fim);
    }

    @Transactional(readOnly = true)
    public List<Compra> listar() {
        return compraRepository.findAllComDetalhes();
    }

    @Transactional(readOnly = true)
    public Optional<Compra> buscarPorId(Long id) {
        return compraRepository.findByIdComDetalhes(id);
    }

    // ========== MÉTODOS DE VALIDAÇÃO ==========

    private void validarCompra(Compra compra) {
        if (compra.getNumeroNota() == null || compra.getNumeroNota().trim().isEmpty()) {
            throw new IllegalArgumentException("O número da nota é obrigatório.");
        }

        if (compra.getDataCompra() == null) {
            throw new IllegalArgumentException("A data da compra é obrigatória.");
        }

        if (compra.getFornecedor() == null || compra.getFornecedor().getIdFornecedor() == null) {
            throw new IllegalArgumentException("O fornecedor é obrigatório.");
        }

        if (compra.getItens() == null || compra.getItens().isEmpty()) {
            throw new IllegalArgumentException("A compra deve ter pelo menos um item.");
        }

        if (compra.getTotalCompra() == null || compra.getTotalCompra().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O total da compra deve ser maior que zero.");
        }

        if (compra.getMovimentarFinanceiro() == null) {
            throw new IllegalArgumentException("As informações financeiras são obrigatórias.");
        }

        if (compra.getMovimentarFinanceiro().getDataVencimento() == null) {
            throw new IllegalArgumentException("A data de vencimento do financeiro é obrigatória.");
        }

        // Validar itens
        for (ItemCompras item : compra.getItens()) {
            validarItem(item);
        }
    }

    private void validarItem(ItemCompras item) {
        if (item.getQuantidade() == null || item.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade do item deve ser maior que zero.");
        }

        if (item.getValorBruto() == null || item.getValorBruto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor bruto do item deve ser maior que zero.");
        }

        if (item.getDerivacao() == null || item.getDerivacao().getIdDerivacao() == null) {
            throw new IllegalArgumentException("Item sem derivação ou sem ID de derivação.");
        }
    }

    // ESTORNO COMPRAS

    @Transactional
    public void estornarCompra(EstornoCompraRequest request) {
        // 1. Validar e buscar a Compra
        Compra compra = compraRepository.findByIdComDetalhes(request.getIdCompra())
                .orElseThrow(() -> new IllegalArgumentException("Compra não encontrada com ID: " + request.getIdCompra()));

        // ... (código que processa itens e cancela o financeiro) ...

        // 2. Processar itens (ajuste de estoque e ItemCompras)
        for (EstornoCompraRequest.ItemEstornoDTO itemEstorno : request.getItensEstorno()) {
            processarEstornoItem(itemEstorno); // Este método reduz a quantidade
        }

        // 3. (OPCIONALMENTE): Se você for recarregar a compra para verificar se todos os itens estão zerados:
        // compra = compraRepository.findByIdComDetalhes(compra.getIdCompra()).get();

        // 4. DEFINIR O NOVO STATUS DA COMPRA
        if ("total".equalsIgnoreCase(request.getTipoEstorno())) {
            compra.setStatus(StatusCompra.ESTORNADA_TOTAL);
        } else {
            // Em um estorno parcial, mudamos o status para PARCIAL
            compra.setStatus(StatusCompra.ESTORNADA_PARCIAL);
        }

        // Salvar a Compra com o novo status
        compraRepository.save(compra);

        // 5. Registrar o histórico de estorno (AUDITORIA) - Mantenha este passo

        // ... (restante da lógica de Movimentação Financeira e EstornoComprasRepository) ...
    }

    /**
     * Ajusta o estoque na Derivacao e reduz a quantidade no ItemCompras original.
     */
    /**
     * Ajusta o estoque na Derivacao e reduz a quantidade no ItemCompras original.
     */
    @Transactional
    private void processarEstornoItem(EstornoCompraRequest.ItemEstornoDTO itemEstornoDTO) {
        // CORRIGIDO: Usar o método com FETCH JOIN para garantir que a Derivacao não seja nula
        ItemCompras itemOriginal = itemComprasRepository.findByIdComDerivacao(itemEstornoDTO.getIdItemCompra())
                .orElseThrow(() -> new IllegalArgumentException("Item de Compra não encontrado com ID: " + itemEstornoDTO.getIdItemCompra()));

        BigDecimal qtdEstorno = itemEstornoDTO.getQuantidadeEstorno();
        Derivacao derivacao = itemOriginal.getDerivacao();

        // 1. Validações de integridade
        if (derivacao == null || derivacao.getIdDerivacao() == null) {
            throw new IllegalStateException("O Item de Compra encontrado não possui uma Derivação válida associada. Não é possível ajustar o estoque.");
        }

        if (qtdEstorno.compareTo(itemOriginal.getQuantidade()) > 0) {
            throw new IllegalArgumentException(String.format("Estorno do item %d excede a quantidade original (Qtd original: %s).",
                    itemEstornoDTO.getIdItemCompra(), itemOriginal.getQuantidade()));
        }

        // 2. Ajustar Estoque (diminuir)
        int quantidadeEstornada = qtdEstorno.intValue();
        Integer estoqueAtual = derivacao.getEstoque() != null ? derivacao.getEstoque() : 0;
        Integer novoEstoque = estoqueAtual - quantidadeEstornada;

        if (novoEstoque < 0) {
            throw new IllegalStateException(String.format("Estoque do SKU %s ficaria negativo (%d) após o estorno de %d unidades.",
                    derivacao.getCodigoSKU(), novoEstoque, quantidadeEstornada));
        }

        derivacao.setEstoque(novoEstoque);
        derivacaoRepository.save(derivacao);

        // 3. Ajustar o ItemCompras original (reduzir a quantidade e recalcular o valor)
        BigDecimal novaQuantidade = itemOriginal.getQuantidade().subtract(qtdEstorno);
        itemOriginal.setQuantidade(novaQuantidade);

        if (novaQuantidade.compareTo(BigDecimal.ZERO) > 0) {
            // Recalcula o valor líquido restante (método já existente no service)
            calcularValorLiquido(itemOriginal);
        } else {
            // Zera o valor se a quantidade restante for zero
            itemOriginal.setValorLiquido(BigDecimal.ZERO);
        }

        itemComprasRepository.save(itemOriginal);

        log.info("Item Compra ID {} ajustado. Estoque: {} → {}. Nova Qtd restante: {}",
                itemOriginal.getIdICompras(), estoqueAtual, novoEstoque, itemOriginal.getQuantidade());
    }
}
