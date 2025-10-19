package br.com.sigvest.api.service;

import br.com.sigvest.api.model.produto.Derivacao;
import br.com.sigvest.api.repository.DerivacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstoqueService {

    private DerivacaoRepository derivacaoRepository;

    @Transactional
    public void adicionarEstoque(Long idProduto, Integer quantidade) {
        log.info("Adicionando {} unidades ao estoque do produto ID: {}", quantidade, idProduto);

        List<Derivacao> derivacoes = derivacaoRepository.findAll()
                .stream()
                .filter(d -> d.getProduto().getIdProduto().equals(idProduto))
                .toList();

        if (derivacoes.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma derivação encontrada para o produto ID: " + idProduto);
        }

        // Distribui o estoque proporcionalmente entre as derivações
        int quantidadePorDerivacao = quantidade / derivacoes.size();
        int resto = quantidade % derivacoes.size();

        for (int i = 0; i < derivacoes.size(); i++) {
            Derivacao derivacao = derivacoes.get(i);
            int quantidadeAdicionar = quantidadePorDerivacao + (i < resto ? 1 : 0);

            int estoqueAtual = derivacao.getEstoque() != null ? derivacao.getEstoque() : 0;
            derivacao.setEstoque(estoqueAtual + quantidadeAdicionar);

            derivacaoRepository.save(derivacao);
            log.info("Estoque atualizado - SKU: {}, Estoque anterior: {}, Estoque atual: {}",
                    derivacao.getCodigoSKU(), estoqueAtual, derivacao.getEstoque());
        }
    }

    @Transactional
    public void atualizarPrecoCusto(Long idProduto, BigDecimal precoCusto) {
        log.info("Atualizando preço de custo para R$ {} no produto ID: {}", precoCusto, idProduto);

        List<Derivacao> derivacoes = derivacaoRepository.findAll()
                .stream()
                .filter(d -> d.getProduto().getIdProduto().equals(idProduto))
                .toList();

        for (Derivacao derivacao : derivacoes) {
            derivacao.setPrecoCusto(precoCusto);

            // Recalcular margem se já houver preço de venda
            if (derivacao.getPrecoVenda() != null && derivacao.getPrecoVenda().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal margem = calcularMargem(precoCusto, derivacao.getPrecoVenda());
                derivacao.setMargemVenda(margem);
            }

            derivacaoRepository.save(derivacao);
        }
    }

    @Transactional
    public void baixarEstoque(Long idDerivacao, Integer quantidade) {
        Derivacao derivacao = derivacaoRepository.findById(idDerivacao)
                .orElseThrow(() -> new IllegalArgumentException("Derivação não encontrada: " + idDerivacao));

        int estoqueAtual = derivacao.getEstoque() != null ? derivacao.getEstoque() : 0;

        if (estoqueAtual < quantidade) {
            throw new IllegalArgumentException(
                    String.format("Estoque insuficiente. Disponível: %d, Solicitado: %d",
                            estoqueAtual, quantidade)
            );
        }

        derivacao.setEstoque(estoqueAtual - quantidade);
        derivacaoRepository.save(derivacao);

        log.info("Baixa de estoque - SKU: {}, Quantidade baixada: {}, Estoque restante: {}",
                derivacao.getCodigoSKU(), quantidade, derivacao.getEstoque());
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularValorTotalEstoque() {
        List<Derivacao> todasDerivacoes = derivacaoRepository.findByEstoqueGreaterThanZero();

        return todasDerivacoes.stream()
                .map(d -> {
                    BigDecimal precoCusto = d.getPrecoCusto() != null ? d.getPrecoCusto() : BigDecimal.ZERO;
                    Integer estoque = d.getEstoque() != null ? d.getEstoque() : 0;
                    return precoCusto.multiply(BigDecimal.valueOf(estoque));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularValorEstoquePorProduto(Long idProduto) {
        List<Derivacao> derivacoes = derivacaoRepository.findAll()
                .stream()
                .filter(d -> d.getProduto().getIdProduto().equals(idProduto))
                .filter(d -> d.getEstoque() != null && d.getEstoque() > 0)
                .toList();

        return derivacoes.stream()
                .map(d -> {
                    BigDecimal precoCusto = d.getPrecoCusto() != null ? d.getPrecoCusto() : BigDecimal.ZERO;
                    return precoCusto.multiply(BigDecimal.valueOf(d.getEstoque()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularMargem(BigDecimal precoCusto, BigDecimal precoVenda) {
        if (precoCusto.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal lucro = precoVenda.subtract(precoCusto);
        return lucro.divide(precoCusto, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}