package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.fornecedor.Fornecedor;
import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping
    public List<Fornecedor> listar() {
        return fornecedorService.listar();
    }
    @PostMapping
    public Fornecedor setUsuario(@RequestBody Fornecedor fornecedor) {
        return fornecedorService.salvar(fornecedor);
    }

}
