package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
