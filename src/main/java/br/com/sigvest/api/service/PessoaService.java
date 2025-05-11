package br.com.sigvest.api.service;

import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa salvar(Pessoa pessoa) {
        if (pessoa.getEndereco() == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo");
        }
        return pessoaRepository.save(pessoa);
    }


    public List<Pessoa> listar(){
        return pessoaRepository.findAll();
    }
}
