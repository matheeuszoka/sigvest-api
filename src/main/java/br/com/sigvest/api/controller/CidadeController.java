package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.service.CidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cidade")
public class CidadeController {

    @Autowired
    private CidadeService cidadeService;

    @GetMapping
    public List<Cidade> listar() {
        return cidadeService.listar();
    }
    @PostMapping
    public Cidade setUsuario(@RequestBody Cidade cidade) {
        return cidadeService.salvar(cidade);
    }
}
