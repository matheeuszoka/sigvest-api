package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.funcionario.Funcionario;
import br.com.sigvest.api.service.EnderecoService.EnderecoHierarquiaService;
import br.com.sigvest.api.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private EnderecoHierarquiaService enderecoHierarquiaService;

    // Criar novo Funcionário
    @PostMapping
    @Transactional
    public ResponseEntity<?> criarFuncionario(@RequestBody Funcionario funcionario) {
        try {
            if (funcionario.getEndereco() != null) {
                Endereco enderecoProcessado = enderecoHierarquiaService
                        .processarHierarquiaEndereco(funcionario.getEndereco());
                funcionario.setEndereco(enderecoProcessado);
            }

            Funcionario funcionarioSalvo = funcionarioService.salvar(funcionario);
            return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioSalvo);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Listar todos os funcionários
    @GetMapping
    public ResponseEntity<List<Funcionario>> listar() {
        List<Funcionario> funcionarios = funcionarioService.listar();
        return ResponseEntity.ok(funcionarios);
    }

    // Buscar funcionário por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Funcionario> funcionario = funcionarioService.buscarPorId(id);
        if (funcionario.isPresent()) {
            return ResponseEntity.ok(funcionario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Funcionário não encontrado"));
        }
    }

    // Buscar funcionário por nome (LIKE)
    @GetMapping("/buscar")
    public ResponseEntity<List<Funcionario>> buscarPorNome(@RequestParam String nome) {
        List<Funcionario> funcionarios = funcionarioService.buscarLikeNome(nome);
        return ResponseEntity.ok(funcionarios);
    }

    // Atualizar funcionário
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizarFuncionario(
            @PathVariable Long id,
            @RequestBody Funcionario funcionarioAtualizado) {
        try {
            Funcionario funcionario = funcionarioService.atualizarFuncionario(id, funcionarioAtualizado);
            return ResponseEntity.ok(funcionario);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        boolean deletado = funcionarioService.deletar(id);
        if (deletado) {
            return ResponseEntity.ok(Map.of("mensagem", "Funcionário deletado com sucesso"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Funcionário não encontrado"));
        }
    }
}