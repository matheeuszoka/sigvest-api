package br.com.sigvest.api.service;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.model.endereco.Estado;
import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.repository.CidadeRepository;
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
    @Autowired
    private CidadeRepository cidadeRepository;

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

        // Validação do estado (deve vir antes da cidade)
        if (pessoa.getEndereco().getCidade().getEstado() == null) {
            throw new IllegalArgumentException("Estado não pode ser nulo");
        }

        // Processar estado primeiro
        Estado estadoRecebido = pessoa.getEndereco().getCidade().getEstado();

        // Validação adicional do estado
        if (estadoRecebido.getUf() == null || estadoRecebido.getUf().trim().isEmpty()) {
            throw new IllegalArgumentException("UF do estado não pode ser nula ou vazia");
        }
        if (estadoRecebido.getNomeEstado() == null || estadoRecebido.getNomeEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do estado não pode ser nulo ou vazio");
        }

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
        }

        // Processar cidade depois do estado
        Cidade cidadeRecebida = pessoa.getEndereco().getCidade();

        // Validação adicional da cidade
        if (cidadeRecebida.getNomeCidade() == null || cidadeRecebida.getNomeCidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da cidade não pode ser nulo ou vazio");
        }

        // Atualizar o estado na cidade recebida antes de buscar/salvar
        cidadeRecebida.setEstado(estadoComId);

        Optional<Cidade> cidadeExistente = cidadeRepository.findByCidade(
                cidadeRecebida.getNomeCidade()
        );

        Cidade cidadeComId;
        if (cidadeExistente.isPresent()) {
            cidadeComId = cidadeExistente.get();
        } else {
            cidadeComId = cidadeRepository.save(cidadeRecebida);
        }

        // Atualizar o endereço com a cidade que tem ID válido
        pessoa.getEndereco().setCidade(cidadeComId);

        return pessoaRepository.save(pessoa);
    }

    public List<Pessoa> listar() {
        return pessoaRepository.findAll();
    }

    public List<Pessoa> buscarLikeNome(String nomeCompleto) {
        return pessoaRepository.buscarLikeNome(nomeCompleto);
    }

    public List<Pessoa> buscarAtribFunc() {
        return pessoaRepository.buscarAtribFunc();
    }

    public List<Pessoa> buscarAtribCli() {
        return pessoaRepository.buscarAtribCli();
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

    public Pessoa atualizarPessoa(Long id, Pessoa pessoaAtualizado) {
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