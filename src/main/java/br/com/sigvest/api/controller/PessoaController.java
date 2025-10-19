package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.service.EnderecoService.EnderecoHierarquiaService;
import br.com.sigvest.api.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("pessoa")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private EnderecoHierarquiaService enderecoHierarquiaService;

    @GetMapping
    public List<Pessoa> listar() {
        return pessoaService.listar();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Pessoa> criarPessoa(@RequestBody Pessoa pessoa) {
        try {
            if (pessoa.getEndereco() != null) {
                Endereco enderecoProcessado = enderecoHierarquiaService.processarHierarquiaEndereco(pessoa.getEndereco());
                pessoa.setEndereco(enderecoProcessado);
            }
            Pessoa pessoaSalvo = pessoaService.salvar(pessoa);
            return ResponseEntity.ok(pessoaSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/likenome/{nomeCompleto}")
    public List<Pessoa> buscarLikeNome(@PathVariable String nomeCompleto) {
        return pessoaService.buscarLikeNome(nomeCompleto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        boolean deletado = pessoaService.deletar(id);
        if (deletado) {
            return ResponseEntity.ok("Pessoa deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrado.");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPessoa(
            @PathVariable Long id,
            @RequestBody Pessoa pessoaAtualizada) {

        try {
            Pessoa pessoaAtualizadaResult = pessoaService.atualizarPessoa(id, pessoaAtualizada);
            return ResponseEntity.ok(pessoaAtualizadaResult);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erro", "Erro interno do servidor"));
        }
    }


    @GetMapping("/{id}")
    public Optional<Pessoa> buscarPorId(@PathVariable Long id) {
        return pessoaService.buscarPorId(id);
    }

}
