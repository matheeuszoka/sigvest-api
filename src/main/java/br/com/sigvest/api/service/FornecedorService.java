package br.com.sigvest.api.service;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.model.endereco.Estado;
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

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private CidadeService cidadeService;

    public Fornecedor salvar(Fornecedor fornecedor) {

        validarFornecedor(fornecedor);

        Estado estadoProcessado = estadoService.ObterOuCriarEstado(
                fornecedor.getEndereco().getCidade().getEstado()
        );

        Cidade cidadeProcessado = cidadeService.ObterOuCriarCidade(fornecedor.getEndereco().getCidade());

        fornecedor.getEndereco().setCidade(cidadeProcessado);

        return fornecedorRepository.save(fornecedor);

    }

    private void validarFornecedor(Fornecedor fornecedor){

        if (fornecedor == null){
            throw new IllegalArgumentException("fornecedorNulo");
        }
    }

    public List<Fornecedor> listar() {
        return fornecedorRepository.findAll();
    }
}
