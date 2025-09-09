package br.com.sigvest.api.service;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.endereco.Estado;
import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.repository.EnderecoRepository.CidadeRepository;
import br.com.sigvest.api.repository.EnderecoRepository.EstadoRepository;
import br.com.sigvest.api.repository.PessoaRepository;
import br.com.sigvest.api.service.EnderecoService.EnderecoHierarquiaService;
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

    @Autowired
    private EnderecoHierarquiaService enderecoHierarquiaService;

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

    public Pessoa atualizarPessoa(Long id, Pessoa pessoaAtualizada) {
        // 1. Buscar pessoa existente
        Pessoa pessoaExistente = pessoaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));

        // 2. Atualizar campos básicos
        atualizarCamposBasicos(pessoaExistente, pessoaAtualizada);

        // 3. Processar endereco se fornecido
        if (pessoaAtualizada.getEndereco() != null) {
            Endereco enderecoProcessado = processarEnderecoParaAtualizacao(
                    pessoaExistente.getEndereco(),
                    pessoaAtualizada.getEndereco()
            );
            pessoaExistente.setEndereco(enderecoProcessado);
        }

        return pessoaRepository.save(pessoaExistente);
    }

    private void atualizarCamposBasicos(Pessoa existente, Pessoa atualizada) {
        if (atualizada.getNomeCompleto() != null) {
            existente.setNomeCompleto(atualizada.getNomeCompleto());
        }
        if (atualizada.getDataNascimento() != null) {
            existente.setDataNascimento(atualizada.getDataNascimento());
        }
        if (atualizada.getCpfcnpj() != null) {
            existente.setCpfcnpj(atualizada.getCpfcnpj());
        }
        if (atualizada.getRg() != null) {
            existente.setRg(atualizada.getRg());
        }
        if (atualizada.getTelefone() != null) {
            existente.setTelefone(atualizada.getTelefone());
        }
        if (atualizada.getEmail() != null) {
            existente.setEmail(atualizada.getEmail());
        }
        if (atualizada.getTipo() != null) {
            existente.setTipo(atualizada.getTipo());
        }
        if (atualizada.getAtrib() != null) {
            existente.setAtrib(atualizada.getAtrib());
        }
    }

    private Endereco processarEnderecoParaAtualizacao(Endereco enderecoExistente, Endereco novoEndereco) {
        try {
            // Validar estrutura do novo endereco
            validarEstruturaEndereco(novoEndereco);

            // Processar hierarquia (Estado -> Cidade -> Endereco)
            Endereco enderecoProcessado = enderecoHierarquiaService.processarHierarquiaEndereco(novoEndereco);

            // Se havia endereco anterior, manter o ID para UPDATE ao invés de INSERT
            if (enderecoExistente != null && enderecoExistente.getIdEndereco() != null) {
                enderecoProcessado.setIdEndereco(enderecoExistente.getIdEndereco());
            }

            return enderecoProcessado;

        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao processar endereço: " + e.getMessage());
        }
    }

    private void validarEstruturaEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo");
        }

        // Validar campos obrigatórios do endereco
        if (endereco.getLogradouro() == null || endereco.getLogradouro().trim().isEmpty()) {
            throw new IllegalArgumentException("Logradouro é obrigatório");
        }

        if (endereco.getCep() == null || endereco.getCep().trim().isEmpty()) {
            throw new IllegalArgumentException("CEP é obrigatório");
        }

        // Validar cidade
        if (endereco.getCidade() == null) {
            throw new IllegalArgumentException("Cidade é obrigatória");
        }

        if (endereco.getCidade().getNomeCidade() == null ||
                endereco.getCidade().getNomeCidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da cidade é obrigatório");
        }

        // Validar estado
        if (endereco.getCidade().getEstado() == null) {
            throw new IllegalArgumentException("Estado é obrigatório");
        }

        if (endereco.getCidade().getEstado().getUf() == null ||
                endereco.getCidade().getEstado().getUf().trim().isEmpty()) {
            throw new IllegalArgumentException("UF é obrigatória");
        }

        if (endereco.getCidade().getEstado().getNomeEstado() == null ||
                endereco.getCidade().getEstado().getNomeEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do estado é obrigatório");
        }
    }


    public Optional<Pessoa> buscarPorId(Long id) {
        return pessoaRepository.findById(id);
    }
}