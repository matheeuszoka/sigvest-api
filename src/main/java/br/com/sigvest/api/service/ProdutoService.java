package br.com.sigvest.api.service;

import br.com.sigvest.api.model.produto.Derivacao;
import br.com.sigvest.api.model.produto.Produto;
import br.com.sigvest.api.model.produto.Roupa.Marca;
import br.com.sigvest.api.model.produto.Roupa.Tamanho;
import br.com.sigvest.api.model.produto.Roupa.tipoCor;
import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
import br.com.sigvest.api.repository.DerivacaoRepository;
import br.com.sigvest.api.repository.ProdutoRepository;
import br.com.sigvest.api.service.RoupaSKUService.MarcaService;
import br.com.sigvest.api.service.RoupaSKUService.TamanhoService;
import br.com.sigvest.api.service.RoupaSKUService.TipoRoupaService;
import br.com.sigvest.api.service.RoupaSKUService.tipoCorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private DerivacaoRepository derivacaoRepository;

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private TipoRoupaService tipoRoupaService;

    @Autowired
    private tipoCorService tipoCorService;

    @Autowired
    private TamanhoService tamanhoService;

    @Transactional
    public Produto salvarProduto(Produto request) {
        if (request.getMarca() == null) {
            throw new IllegalArgumentException("Marca é obrigatória");
        }

        // Processar apenas a lista "derivacoes"
        List<Derivacao> derivacoesEntrada = request.getDerivacoes();
        if (derivacoesEntrada == null || derivacoesEntrada.isEmpty()) {
            throw new IllegalArgumentException("Pelo menos uma derivação é obrigatória");
        }

        // Resolver/garantir Marca
        Marca marca = marcaService.buscarOuCriar(request.getMarca().getMarca());

        // Encontrar ou criar o Produto "pai" (único por nome + marca)
        Produto produto = produtoRepository
                .findByNomeProdutoAndMarca(request.getNomeProduto(), marca)
                .orElseGet(() -> {
                    Produto p = new Produto();
                    p.setNomeProduto(request.getNomeProduto());
                    p.setMarca(marca);
                    return produtoRepository.save(p);
                });

        // Processar cada variação (Derivacao) para este Produto
        for (Derivacao dReq : derivacoesEntrada) {
            if (dReq == null) continue;

            // VALIDAR E RESOLVER ENTIDADES OBRIGATÓRIAS
            if (dReq.getTipoRoupa() == null || dReq.getTipoCor() == null || dReq.getTamanho() == null) {
                throw new IllegalArgumentException("TipoRoupa, TipoCor e Tamanho são obrigatórios para cada derivação");
            }

            tipoRoupa tr = tipoRoupaService.buscarOuCriar(dReq.getTipoRoupa().getTipoRoupa());
            tipoCor tc = tipoCorService.buscarOuCriar(dReq.getTipoCor().getNomeCor());
            Tamanho tm = tamanhoService.buscarOuCriar(dReq.getTamanho().getNomeTamanho());

            // VALIDAR SE AS ENTIDADES FORAM CRIADAS CORRETAMENTE
            if (tr == null || tc == null || tm == null) {
                throw new IllegalArgumentException("Erro ao resolver entidades relacionadas");
            }

            // GERAR SKU AUTOMATICAMENTE SE NÃO FORNECIDO OU VAZIO
            String skuFinal = dReq.getCodigoSKU();
            if (skuFinal == null || skuFinal.trim().isEmpty()) {
                skuFinal = gerarSKUAutomatico(request.getNomeProduto(), marca, tr, tc, tm);

                // GARANTIR QUE O SKU FOI GERADO
                if (skuFinal == null || skuFinal.trim().isEmpty()) {
                    skuFinal = gerarSKUFallback();
                }

                // Verificar se SKU gerado já existe e incrementar se necessário
                while (derivacaoRepository.findByCodigoSKU(skuFinal).isPresent()) {
                    skuFinal = incrementarSKU(skuFinal);
                }
            }

            // VALIDAÇÃO FINAL DO SKU
            if (skuFinal == null || skuFinal.trim().isEmpty()) {
                throw new IllegalArgumentException("Não foi possível gerar SKU para a derivação");
            }

            // Verificar se combinação já existe
            Optional<Derivacao> existente = derivacaoRepository
                    .findByProdutoAndTipoRoupaAndTipoCorAndTamanho(produto, tr, tc, tm);

            if (existente.isPresent()) {
                // Atualiza estoque da derivação existente
                Derivacao dx = existente.get();
                int inc = dReq.getEstoque() == null ? 0 : dReq.getEstoque();
                dx.setEstoque((dx.getEstoque() == null ? 0 : dx.getEstoque()) + inc);
                if (dReq.getPrecoCusto() != null) dx.setPrecoCusto(dReq.getPrecoCusto());
                if (dReq.getPrecoVenda() != null) dx.setPrecoVenda(dReq.getPrecoVenda());
                if (dReq.getMargemVenda() != null) dx.setMargemVenda(dReq.getMargemVenda());
                if (dReq.getCodigoVenda() != null) dx.setCodigoVenda(dReq.getCodigoVenda());
                derivacaoRepository.save(dx);
            } else {
                // Cria nova variação (SKU) com validações
                Derivacao nova = new Derivacao();
                nova.setProduto(produto);
                nova.setCodigoSKU(skuFinal); // SKU GARANTIDO NÃO NULO
                nova.setCodigoVenda(dReq.getCodigoVenda());
                nova.setPrecoCusto(dReq.getPrecoCusto() != null ? dReq.getPrecoCusto() : java.math.BigDecimal.ZERO);
                nova.setPrecoVenda(dReq.getPrecoVenda() != null ? dReq.getPrecoVenda() : java.math.BigDecimal.ZERO);
                nova.setMargemVenda(dReq.getMargemVenda() != null ? dReq.getMargemVenda() : java.math.BigDecimal.ZERO);
                nova.setEstoque(dReq.getEstoque() != null ? dReq.getEstoque() : 0);
                nova.setTipoRoupa(tr);
                nova.setTipoCor(tc);
                nova.setTamanho(tm);

                // VALIDAÇÃO FINAL ANTES DE SALVAR
                if (nova.getCodigoSKU() == null || nova.getCodigoSKU().trim().isEmpty()) {
                    throw new IllegalArgumentException("SKU não pode ser nulo ao salvar derivação");
                }

                derivacaoRepository.save(nova);
            }
        }

        return produtoRepository.findById(produto.getIdProduto()).orElse(produto);
    }

    /**
     * Gera SKU automático com validações robustas
     */
    private String gerarSKUAutomatico(String nomeProduto, Marca marca,
                                      tipoRoupa tipoRoupa, tipoCor tipoCor,
                                      Tamanho tamanho) {
        try {
            // VALIDAR ENTRADAS
            if (nomeProduto == null || marca == null || tipoRoupa == null || tipoCor == null || tamanho == null) {
                return gerarSKUFallback();
            }

            String tipoAbrev = abreviarTexto(tipoRoupa.getTipoRoupa(), 3);
            String marcaAbrev = abreviarTexto(marca.getMarca(), 3);
            String corAbrev = abreviarTexto(tipoCor.getNomeCor(), 2);
            String tamanhoAbrev = abreviarTamanho(tamanho.getNomeTamanho());
            String sequencia = gerarSequencia(tipoAbrev, marcaAbrev, corAbrev, tamanhoAbrev);

            String sku = String.format("%s-%s-%s-%s-%s",
                            tipoAbrev, marcaAbrev, corAbrev, tamanhoAbrev, sequencia)
                    .toUpperCase();

            // VALIDAR SE SKU FOI GERADO CORRETAMENTE
            if (sku.contains("NULL") || sku.length() < 10) {
                return gerarSKUFallback();
            }

            return sku;
        } catch (Exception e) {
            return gerarSKUFallback();
        }
    }

    /**
     * SKU de fallback para casos de erro
     */
    private String gerarSKUFallback() {
        return "AUTO-" + System.currentTimeMillis();
    }

    private String abreviarTexto(String texto, int maxChars) {
        if (texto == null || texto.trim().isEmpty()) {
            return "XXX".substring(0, maxChars);
        }

        String textoLimpo = texto.replaceAll("[^a-zA-Z]", "").toUpperCase();
        if (textoLimpo.isEmpty()) {
            return "XXX".substring(0, maxChars);
        }

        if (textoLimpo.length() <= maxChars) {
            return String.format("%-" + maxChars + "s", textoLimpo).replace(' ', 'X');
        }

        // Remove vogais para abreviar
        String semVogais = textoLimpo.replaceAll("[AEIOU]", "");

        if (semVogais.length() >= maxChars) {
            return semVogais.substring(0, maxChars);
        }

        // Se ainda for pequeno, pega do texto original
        return textoLimpo.substring(0, Math.min(maxChars, textoLimpo.length()));
    }

    private String abreviarTamanho(String tamanho) {
        if (tamanho == null || tamanho.trim().isEmpty()) {
            return "U";
        }

        Map<String, String> mapeamento = Map.of(
                "PEQUENO", "P", "MÉDIO", "M", "MEDIO", "M",
                "GRANDE", "G", "EXTRA GRANDE", "XG", "EXTRAGRANDE", "XG",
                "EXTRA PEQUENO", "XP", "EXTRAPEQUENO", "XP"
        );

        String tamanhoUpper = tamanho.toUpperCase().trim();
        String mapeado = mapeamento.get(tamanhoUpper);
        if (mapeado != null) return mapeado;

        if (tamanhoUpper.length() <= 2) return tamanhoUpper;

        return tamanhoUpper.substring(0, Math.min(2, tamanhoUpper.length()));
    }

    private String gerarSequencia(String tipo, String marca, String cor, String tamanho) {
        try {
            String padrao = String.format("%s-%s-%s-%s", tipo, marca, cor, tamanho);
            String ultimoSKU = derivacaoRepository.findLastSKUByPattern(padrao + "%");

            if (ultimoSKU == null) {
                return "001";
            }

            String[] partes = ultimoSKU.split("-");
            if (partes.length >= 5) {
                try {
                    int ultimoNumero = Integer.parseInt(partes[4]);
                    return String.format("%03d", ultimoNumero + 1);
                } catch (NumberFormatException e) {
                    return "001";
                }
            }

            return "001";
        } catch (Exception e) {
            return "001";
        }
    }

    private String incrementarSKU(String sku) {
        if (sku == null) return gerarSKUFallback();

        String[] partes = sku.split("-");
        if (partes.length >= 5) {
            try {
                int seq = Integer.parseInt(partes[4]) + 1;
                partes[4] = String.format("%03d", seq);
                return String.join("-", partes);
            } catch (NumberFormatException e) {
                return sku + "-" + System.currentTimeMillis();
            }
        }
        return sku + "-" + System.currentTimeMillis();
    }

    // Métodos auxiliares mantidos como estavam
    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public boolean deletar(Long id) {
        return produtoRepository.findById(id).map(p -> {
            produtoRepository.delete(p);
            return true;
        }).orElse(false);
    }

    public List<Produto> buscarProdutos(String termo) {
        return produtoRepository.buscarProdutos(termo);
    }

    public Optional<Produto> buscarPorSKU(String codigoSKU) {
        return derivacaoRepository.findByCodigoSKU(codigoSKU).map(Derivacao::getProduto);
    }

    @Transactional
    public Derivacao atualizarEstoque(Long idDerivacao, Integer novoEstoque) {
        Derivacao d = derivacaoRepository.findById(idDerivacao)
                .orElseThrow(() -> new IllegalArgumentException("Derivação não encontrada"));
        d.setEstoque(novoEstoque);
        return derivacaoRepository.save(d);
    }
}
