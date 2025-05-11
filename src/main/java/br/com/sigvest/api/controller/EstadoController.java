package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.endereco.Estado;
import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("estado")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    @GetMapping
    public List<Estado> listar() {
        return estadoService.listar();
    }
    @PostMapping
    public Estado setUsuario(@RequestBody Estado estado) {
        return estadoService.salvar(estado);
    }
}
