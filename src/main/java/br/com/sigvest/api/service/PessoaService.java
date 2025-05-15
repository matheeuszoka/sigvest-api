package br.com.sigvest.api.service;

import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Pessoa> buscarLikeNome(String nomeCompleto){
        return pessoaRepository.buscarLikeNome(nomeCompleto);
    }

    public boolean deletar(Long id) {
        Optional<Pessoa> usuarioOptional = pessoaRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            pessoaRepository.delete(usuarioOptional.get());
            return true;
        } else {
            return false;
        }
    }
    public  Pessoa atualizarPessoa(Long id, Pessoa pessoaAtualizado) {
        Pessoa pessoaExistente = pessoaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));

        pessoaExistente.setNomeCompleto(pessoaAtualizado.getNomeCompleto());
        pessoaExistente.setDataNascimento(pessoaAtualizado.getDataNascimento());
        pessoaExistente.setCpfcnpj(pessoaAtualizado.getCpfcnpj());
        pessoaExistente.setRg(pessoaAtualizado.getRg());
        pessoaExistente.setTelefone(pessoaAtualizado.getTelefone());
        pessoaExistente.setEmail(pessoaAtualizado.getEmail());
        pessoaExistente.setTipo(pessoaAtualizado.getTipo());
        pessoaExistente.setAtrib(pessoaAtualizado.getAtrib());
        pessoaExistente.setEndereco(pessoaAtualizado.getEndereco());

        return pessoaRepository.save(pessoaExistente);
    }
}

