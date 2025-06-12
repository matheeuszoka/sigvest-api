package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public Pessoa setPessoa(@RequestBody Pessoa pessoa) {
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
            return ResponseEntity.ok("Pessoa deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrado.");
        }
    }

    @GetMapping("/atrib/funcionario")
    public List<Pessoa> buscarAtribFunc( ){
        return pessoaService.buscarAtribFunc();
    }


    @GetMapping("/atrib/cliente")
    public List<Pessoa> buscarAtribCli( ){
        return pessoaService.buscarAtribCli();
    }

    @PutMapping("/{id}")  // Em vez de "/atualizarPessoa/{id}"
    public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable Long id, @RequestBody Pessoa pessoa){
        Pessoa pessoaUp = pessoaService.atualizarPessoa(id, pessoa);
        return ResponseEntity.ok(pessoaUp);
    }
    @GetMapping("/{id}")
    public Optional<Pessoa> buscarPorId(@PathVariable Long id) {
        return pessoaService.buscarPorId(id);
    }

}
