package br.com.sigvest.api.service;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    public Cidade ObterOuCriarCidade(Cidade cidade) {
        validarCidade(cidade);

        Optional<Cidade> cidadeExistente = buscarCidadeExistente(cidade);

        if (cidadeExistente.isPresent()) {
            return cidadeExistente.get();
        } else {
            return cidadeRepository.save(cidade);
        }
    }

    private void validarCidade(Cidade cidade) {
        if (cidade.getNomeCidade() == null || cidade.getNomeCidade().trim().isEmpty()) {
            throw new IllegalArgumentException("CidadeVazia");
        }
    }

    private Optional<Cidade>buscarCidadeExistente(Cidade cidade){
        return cidadeRepository.findByCidade(cidade.getNomeCidade());
    }

    public Cidade salvar(Cidade cidade) {
        return cidadeRepository.save(cidade);
    }

    public List<Cidade> listar() {
        return cidadeRepository.findAll();
    }

    public List<Cidade> buscarLikeCidade(String nomeCidade) {
        return cidadeRepository.buscarLikeCidade(nomeCidade);
    }

}
