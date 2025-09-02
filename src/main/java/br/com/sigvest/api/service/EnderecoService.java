package br.com.sigvest.api.service;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.endereco.Estado;
import br.com.sigvest.api.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    /**
     * Valida se o endereço já existe no banco
     */
    public Optional<Endereco> buscarEnderecoExistente(Endereco endereco) {
        return enderecoRepository.findByLogradouroAndNumeroAndCepAndCidade(
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getCep(),
                endereco.getCidade()
        );
    }

    /**
     * Verifica se dois endereços são iguais
     */
    public boolean enderecosIguais(Endereco endereco1, Endereco endereco2) {
        if (endereco1 == null || endereco2 == null) {
            return false;
        }

        return Objects.equals(endereco1.getLogradouro(), endereco2.getLogradouro()) &&
                Objects.equals(endereco1.getNumero(), endereco2.getNumero()) &&
                Objects.equals(endereco1.getCep(), endereco2.getCep()) &&
                Objects.equals(endereco1.getBairro(), endereco2.getBairro()) &&
                cidadesIguais(endereco1.getCidade(), endereco2.getCidade());
    }

    private boolean cidadesIguais(Cidade cidade1, Cidade cidade2) {
        if (cidade1 == null || cidade2 == null) {
            return false;
        }

        return Objects.equals(cidade1.getNomeCidade(), cidade2.getNomeCidade()) &&
                estadosIguais(cidade1.getEstado(), cidade2.getEstado());
    }

    private boolean estadosIguais(Estado estado1, Estado estado2) {
        if (estado1 == null || estado2 == null) {
            return false;
        }

        return Objects.equals(estado1.getUf(), estado2.getUf()) &&
                Objects.equals(estado1.getNomeEstado(), estado2.getNomeEstado());
    }
}
