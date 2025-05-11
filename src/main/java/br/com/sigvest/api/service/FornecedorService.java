package br.com.sigvest.api.service;

import br.com.sigvest.api.model.fornecedor.Fornecedor;
import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {


    @Autowired
    private FornecedorRepository fornecedorRepository;

    public Fornecedor salvar(Fornecedor fornecedor) {
        if (fornecedor.getEndereco() == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo");
        }
        return fornecedorRepository.save(fornecedor);
    }


    public List<Fornecedor> listar(){
        return fornecedorRepository.findAll();
    }
}
