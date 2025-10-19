package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.fornecedor.Fornecedor;
import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.service.EnderecoService.EnderecoHierarquiaService;
import br.com.sigvest.api.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private EnderecoHierarquiaService enderecoHierarquiaService;

    //Novo Fornecedor
    @PostMapping
    @Transactional
    public ResponseEntity<Fornecedor> criarFornecedor(@RequestBody Fornecedor fornecedor) {
        try {
            if (fornecedor.getEndereco() != null) {
                Endereco enderecoProcessado = enderecoHierarquiaService
                        .processarHierarquiaEndereco(fornecedor.getEndereco());
                fornecedor.setEndereco(enderecoProcessado);
            }

            Fornecedor fornecedorSalvo = fornecedorService.salvar(fornecedor);

            return ResponseEntity.ok(fornecedorSalvo);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Lista Todos os fornecedores
    @GetMapping
    public List<Fornecedor> listar() {
        return fornecedorService.listar();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        boolean deletado = fornecedorService.deletar(id);
        if (deletado) {
            return ResponseEntity.ok("Fornecedor deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fornecedor não encontrado.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarFornecedor(
            @PathVariable Long id,
            @RequestBody Fornecedor fornecedorAtualizada) {

        try {
            Fornecedor fornecedorAtualizadaResult = fornecedorService.atualizarFornecedor(id, fornecedorAtualizada);
            return ResponseEntity.ok(fornecedorAtualizadaResult);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erro", "Erro interno do servidor"));
        }
    }

    @GetMapping("/likenome/{razaoSocial}")
    public List<Fornecedor> buscarLikeFornecedor(@PathVariable String razaoSocial) {
        return fornecedorService.buscarLikeFornecedor(razaoSocial);
    }

    //Busca Fornecedor Por ID
    @GetMapping("/{id}")
    public Optional<Fornecedor> buscarPorId(@PathVariable Long id) {
        return fornecedorService.buscarPorId(id);
    }
}
