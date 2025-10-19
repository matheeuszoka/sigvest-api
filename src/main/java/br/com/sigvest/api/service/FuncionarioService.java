package br.com.sigvest.api.service;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.endereco.Estado;
import br.com.sigvest.api.model.funcionario.Cargo;
import br.com.sigvest.api.model.funcionario.Funcionario;
import br.com.sigvest.api.repository.CargoRepository;
import br.com.sigvest.api.repository.EnderecoRepository.CidadeRepository;
import br.com.sigvest.api.repository.EnderecoRepository.EstadoRepository;
import br.com.sigvest.api.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        // Validação básica
        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionario não pode ser nulo");
        }

        // Validar e buscar cargo existente
        if (funcionario.getCargo() != null && funcionario.getCargo().getIdCargo() != null) {
            Optional<Cargo> cargoExistente = cargoRepository.findById(funcionario.getCargo().getIdCargo());
            if (cargoExistente.isEmpty()) {
                throw new IllegalArgumentException("Cargo com ID " + funcionario.getCargo().getIdCargo() + " não encontrado");
            }
            funcionario.setCargo(cargoExistente.get());
        }

        // Validação do endereço
        if (funcionario.getEndereco() == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo");
        }

        // Validação da cidade
        if (funcionario.getEndereco().getCidade() == null) {
            throw new IllegalArgumentException("Cidade não pode ser nula");
        }

        // Validação do estado
        if (funcionario.getEndereco().getCidade().getEstado() == null) {
            throw new IllegalArgumentException("Estado não pode ser nulo");
        }

        // Processar estado primeiro
        Estado estadoRecebido = funcionario.getEndereco().getCidade().getEstado();

        if (estadoRecebido.getUf() == null || estadoRecebido.getUf().trim().isEmpty()) {
            throw new IllegalArgumentException("UF do estado não pode ser nula ou vazia");
        }

        if (estadoRecebido.getNomeEstado() == null || estadoRecebido.getNomeEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do estado não pode ser nulo ou vazio");
        }

        // Verificar se o estado já existe
        Optional<Estado> estadoExistente = estadoRepository.findByUfOrNomeEstadoIgnoreCase(
                estadoRecebido.getUf(),
                estadoRecebido.getNomeEstado()
        );

        Estado estadoComId;
        if (estadoExistente.isPresent()) {
            estadoComId = estadoExistente.get();
        } else {
            estadoComId = estadoRepository.save(estadoRecebido);
        }

        // Processar cidade
        Cidade cidadeRecebida = funcionario.getEndereco().getCidade();

        if (cidadeRecebida.getNomeCidade() == null || cidadeRecebida.getNomeCidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da cidade não pode ser nulo ou vazio");
        }

        cidadeRecebida.setEstado(estadoComId);
        Optional<Cidade> cidadeExistente = cidadeRepository.findByCidade(cidadeRecebida.getNomeCidade());

        Cidade cidadeComId;
        if (cidadeExistente.isPresent()) {
            cidadeComId = cidadeExistente.get();
        } else {
            cidadeComId = cidadeRepository.save(cidadeRecebida);
        }

        // Atualizar o endereço
        funcionario.getEndereco().setCidade(cidadeComId);
        return funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> listar() {
        return funcionarioRepository.findAll();
    }

    public List<Funcionario> buscarLikeNome(String nomeCompleto) {
        return funcionarioRepository.buscarLikeNome(nomeCompleto);
    }

    @Transactional
    public boolean deletar(Long id) {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(id);
        if (funcionarioOptional.isPresent()) {
            funcionarioRepository.delete(funcionarioOptional.get());
            return true;
        }
        return false;
    }

    @Transactional
    public Funcionario atualizarFuncionario(Long id, Funcionario funcionarioAtualizado) {
        Funcionario funcionarioExistente = funcionarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));

        // Atualizar campos básicos
        if (funcionarioAtualizado.getNomeCompleto() != null) {
            funcionarioExistente.setNomeCompleto(funcionarioAtualizado.getNomeCompleto());
        }
        if (funcionarioAtualizado.getDataNascimento() != null) {
            funcionarioExistente.setDataNascimento(funcionarioAtualizado.getDataNascimento());
        }
        if (funcionarioAtualizado.getCpfFunc() != null) {
            funcionarioExistente.setCpfFunc(funcionarioAtualizado.getCpfFunc());
        }
        if (funcionarioAtualizado.getRg() != null) {
            funcionarioExistente.setRg(funcionarioAtualizado.getRg());
        }
        if (funcionarioAtualizado.getTelefone() != null) {
            funcionarioExistente.setTelefone(funcionarioAtualizado.getTelefone());
        }
        if (funcionarioAtualizado.getEmail() != null) {
            funcionarioExistente.setEmail(funcionarioAtualizado.getEmail());
        }

        return funcionarioRepository.save(funcionarioExistente);
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        return funcionarioRepository.findById(id);
    }
}
