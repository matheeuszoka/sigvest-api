package br.com.sigvest.api.service;

import br.com.sigvest.api.model.produto.Derivacao;
import br.com.sigvest.api.model.produto.Roupa.Tamanho;
import br.com.sigvest.api.model.produto.Roupa.tipoCor;
import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
import br.com.sigvest.api.repository.DerivacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DerivacaoService {

    @Autowired
    private DerivacaoRepository derivacaoRepository;

    public Derivacao salvarDerivacao(Derivacao derivacao) {
        // Verificar se já existe uma derivação com as mesmas características
        if (derivacao.getTipoRoupa() != null && derivacao.getTipoCor() != null && derivacao.getTamanho() != null) {
            Optional<Derivacao> derivacaoExistente = derivacaoRepository
                    .findByTipoRoupaAndTipoCorAndTamanho(
                            derivacao.getTipoRoupa(),
                            derivacao.getTipoCor(),
                            derivacao.getTamanho()
                    );

            if (derivacaoExistente.isPresent()) {
                throw new IllegalArgumentException(
                        "Já existe uma derivação para " +
                                derivacao.getTipoRoupa().getTipoRoupa() + " " +
                                derivacao.getTipoCor().getNomeCor() + " " +
                                "tamanho " + derivacao.getTamanho().getNomeTamanho()
                );
            }
        }

        // Verificar unicidade do código SKU
        if (derivacao.getCodigoSKU() != null && !derivacao.getCodigoSKU().isEmpty()) {
            Optional<Derivacao> skuExistente = derivacaoRepository.findByCodigoSKU(derivacao.getCodigoSKU());
            if (skuExistente.isPresent()) {
                throw new IllegalArgumentException("Código SKU já existe: " + derivacao.getCodigoSKU());
            }
        }

        // Verificar unicidade do código de venda
        if (derivacao.getCodigoVenda() != null && !derivacao.getCodigoVenda().isEmpty()) {
            Optional<Derivacao> codigoExistente = derivacaoRepository.findByCodigoVenda(derivacao.getCodigoVenda());
            if (codigoExistente.isPresent()) {
                throw new IllegalArgumentException("Código de venda já existe: " + derivacao.getCodigoVenda());
            }
        }

        return derivacaoRepository.save(derivacao);
    }

    public Derivacao atualizarEstoque(Long idDerivacao, Integer novoEstoque) {
        Derivacao derivacao = derivacaoRepository.findById(idDerivacao)
                .orElseThrow(() -> new IllegalArgumentException("Derivação não encontrada"));

        derivacao.setEstoque(novoEstoque);
        return derivacaoRepository.save(derivacao);
    }

    public List<Derivacao> listarPorTipoRoupa(tipoRoupa tipo) {
        return derivacaoRepository.findByTipoRoupa(tipo);
    }

    public List<Derivacao> listarComEstoque() {
        return derivacaoRepository.findByEstoqueGreaterThanZero();
    }

    public Optional<Derivacao> buscarPorSKU(String codigoSKU) {
        return derivacaoRepository.findByCodigoSKU(codigoSKU);
    }
}
