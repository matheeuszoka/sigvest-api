package br.com.sigvest.api.service;

import br.com.sigvest.api.model.endereco.Estado;
import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.repository.EstadoRepository;
import br.com.sigvest.api.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    public Pessoa salvar(Pessoa pessoa) {
        // Validação básica da pessoa
        if (pessoa == null) {
            throw new IllegalArgumentException("Pessoa não pode ser nula");
        }

        // Validação do endereço
        if (pessoa.getEndereco() == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo");
        }

        // Validação da cidade
        if (pessoa.getEndereco().getCidade() == null) {
            throw new IllegalArgumentException("Cidade não pode ser nula");
        }

        // Validação do estado
        if (pessoa.getEndereco().getCidade().getEstado() == null) {
            throw new IllegalArgumentException("Estado não pode ser nulo");
        }

        Estado estadoRecebido = pessoa.getEndereco().getCidade().getEstado();

        // Verificar se o estado já existe no banco pela UF e nome (case insensitive)
        Optional<Estado> estadoExistente = estadoRepository.findByUfOrNomeEstadoIgnoreCase(
                estadoRecebido.getUf(),
                estadoRecebido.getNomeEstado()
        );

        Estado estadoComId;
        if (estadoExistente.isPresent()) {
            // Estado já existe - usa o ID do estado existente
            estadoComId = estadoExistente.get();
        } else {
            // Estado não existe - salva no banco e obtém o ID gerado
            estadoComId = estadoRepository.save(estadoRecebido);
            pessoa.getEndereco().getCep();
        }

        // Atualiza o estado na cidade com o estado que tem ID válido (existente ou novo salvo)
        pessoa.getEndereco().getCidade().setEstado(estadoComId);

        return pessoaRepository.save(pessoa);
    }    public List<Pessoa> listar(){
        return pessoaRepository.findAll();
    }

    public List<Pessoa> buscarLikeNome(String nomeCompleto){
        return pessoaRepository.buscarLikeNome(nomeCompleto);
    }

    public List<Pessoa> buscarAtrib(){
        return pessoaRepository.buscarAtrib();
    }

    public boolean deletar(Long id) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);
        if (pessoaOptional.isPresent()) {
            pessoaRepository.delete(pessoaOptional.get());
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

    public Optional<Pessoa> buscarPorId(Long id) {
        return pessoaRepository.findById(id);
    }

}

