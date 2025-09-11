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

            tipoRoupa tr = tipoRoupaService.buscarOuCriar(
                    dReq.getTipoRoupa() != null ? dReq.getTipoRoupa().getTipoRoupa() : null
            );
            tipoCor tc = tipoCorService.buscarOuCriar(
                    dReq.getTipoCor() != null ? dReq.getTipoCor().getNomeCor() : null
            );
            Tamanho tm = tamanhoService.buscarOuCriar(
                    dReq.getTamanho() != null ? dReq.getTamanho().getNomeTamanho() : null
            );

            // USAR MÉTODO COM PRODUTO (modelo 1:N)
            Optional<Derivacao> existente = derivacaoRepository
                    .findByProdutoAndTipoRoupaAndTipoCorAndTamanho(produto, tr, tc, tm);

            if (existente.isPresent()) {
                // Atualiza estoque
                Derivacao dx = existente.get();
                int inc = dReq.getEstoque() == null ? 0 : dReq.getEstoque();
                dx.setEstoque((dx.getEstoque() == null ? 0 : dx.getEstoque()) + inc);
                if (dReq.getPrecoCusto() != null) dx.setPrecoCusto(dReq.getPrecoCusto());
                if (dReq.getPrecoVenda() != null) dx.setPrecoVenda(dReq.getPrecoVenda());
                if (dReq.getMargemVenda() != null) dx.setMargemVenda(dReq.getMargemVenda());
                if (dReq.getCodigoVenda() != null) dx.setCodigoVenda(dReq.getCodigoVenda());
                derivacaoRepository.save(dx);
            } else {
                // Cria nova variação (SKU)
                Derivacao nova = new Derivacao();
                nova.setProduto(produto);
                nova.setCodigoSKU(dReq.getCodigoSKU());
                nova.setCodigoVenda(dReq.getCodigoVenda());
                nova.setPrecoCusto(dReq.getPrecoCusto());
                nova.setPrecoVenda(dReq.getPrecoVenda());
                nova.setMargemVenda(dReq.getMargemVenda());
                nova.setEstoque(dReq.getEstoque());
                nova.setTipoRoupa(tr);
                nova.setTipoCor(tc);
                nova.setTamanho(tm);
                derivacaoRepository.save(nova);
            }
        }

        return produtoRepository.findById(produto.getIdProduto()).orElse(produto);
    }

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

    public List<Derivacao> listarDerivacoes(String nomeProduto) {
        return produtoRepository.findByNomeProdutoContainingIgnoreCase(nomeProduto)
                .stream().findFirst()
                .map(derivacaoRepository::findByProduto)
                .orElseGet(ArrayList::new);
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
