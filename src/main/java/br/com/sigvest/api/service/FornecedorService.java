package br.com.sigvest.api.service;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.endereco.Estado;
import br.com.sigvest.api.model.fornecedor.Fornecedor;
import br.com.sigvest.api.repository.FornecedorRepository;
import br.com.sigvest.api.service.EnderecoService.CidadeService;
import br.com.sigvest.api.service.EnderecoService.EnderecoHierarquiaService;
import br.com.sigvest.api.service.EnderecoService.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {


    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private CidadeService cidadeService;

    @Autowired
    private EnderecoHierarquiaService enderecoHierarquiaService;

    private void validarFornecedor(Fornecedor fornecedor) {

        if (fornecedor == null) {
            throw new IllegalArgumentException("fornecedorNulo");
        }
    }

    //Novo fornecedor
    public Fornecedor salvar(Fornecedor fornecedor) {

        validarFornecedor(fornecedor);

        Estado estadoProcessado = estadoService.ObterOuCriarEstado(
                fornecedor.getEndereco().getCidade().getEstado()
        );

        Cidade cidadeProcessado = cidadeService.ObterOuCriarCidade(fornecedor.getEndereco().getCidade());

        fornecedor.getEndereco().setCidade(cidadeProcessado);

        return fornecedorRepository.save(fornecedor);

    }

    public boolean deletar(Long id) {
        Optional<Fornecedor> fornecedorOptional = fornecedorRepository.findById(id);
        if (fornecedorOptional.isPresent()) {
            fornecedorRepository.delete(fornecedorOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public Fornecedor atualizarFornecedor(Long id, Fornecedor fornecedorAtualizada) {
        // 1. Buscar pessoa existente
        Fornecedor fornecedorExistente = fornecedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrada"));

        // 2. Atualizar campos básicos
        atualizarCamposBasicos(fornecedorExistente, fornecedorAtualizada);

        // 3. Processar endereco se fornecido
        if (fornecedorAtualizada.getEndereco() != null) {
            Endereco enderecoProcessado = processarEnderecoParaAtualizacao(
                    fornecedorExistente.getEndereco(),
                    fornecedorAtualizada.getEndereco()
            );
            fornecedorExistente.setEndereco(enderecoProcessado);
        }

        return fornecedorRepository.save(fornecedorExistente);
    }

    private void atualizarCamposBasicos(Fornecedor existente, Fornecedor atualizada) {
        if (atualizada.getNomeFantasia() != null) {
            existente.setNomeFantasia(atualizada.getNomeFantasia());
        }
        if (atualizada.getRazaoSocial() != null) {
            existente.setRazaoSocial(atualizada.getRazaoSocial());
        }
        if (atualizada.getCnpjFornecedor() != null) {
            existente.setCnpjFornecedor(atualizada.getCnpjFornecedor());
        }
        if (atualizada.getInscricaoEstadual() != null) {
            existente.setInscricaoEstadual(atualizada.getInscricaoEstadual());
        }
        if (atualizada.getTelefone() != null) {
            existente.setTelefone(atualizada.getTelefone());
        }
        if (atualizada.getEmail() != null) {
            existente.setEmail(atualizada.getEmail());
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


    //Lista todos os fornecedores
    public List<Fornecedor> listar() {
        return fornecedorRepository.findAll();
    }

    //Lista forncedor por id
    public Optional<Fornecedor> buscarPorId(Long id) {
        return fornecedorRepository.findById(id);
    }

}
