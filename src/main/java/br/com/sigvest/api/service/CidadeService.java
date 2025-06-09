package br.com.sigvest.api.service;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    public Cidade salvar(Cidade cidade){
        return cidadeRepository.save(cidade);
    }

    public List<Cidade> listar(){
        return cidadeRepository.findAll();
    }

    public List<Cidade> buscarLikeCidade(String nomeCidade){
        return cidadeRepository.buscarLikeCidade(nomeCidade);
    }

}
