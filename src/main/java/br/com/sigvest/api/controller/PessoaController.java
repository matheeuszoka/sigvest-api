package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pessoa")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping
    public List<Pessoa> listar() {
        return pessoaService.listar();
    }

    @PostMapping
    public Pessoa setUsuario(@RequestBody Pessoa pessoa) {
        return pessoaService.salvar(pessoa);
    }

    @GetMapping("/likenome/{nomeCompleto}")
    public List<Pessoa> buscarLikeNome(@PathVariable String nomeCompleto){
        return pessoaService.buscarLikeNome(nomeCompleto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        boolean deletado = pessoaService.deletar(id);
        if (deletado) {
            return ResponseEntity.ok("Usuário deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
    }

    @PutMapping("/atualizarPessoa/{id}")
    public ResponseEntity<Pessoa>atualizarPessoa(@PathVariable Long id, @RequestBody Pessoa pessoa){
        Pessoa pessoaUp = pessoaService.atualizarPessoa(id, pessoa);
        return ResponseEntity.ok(pessoaUp);
    }


}
