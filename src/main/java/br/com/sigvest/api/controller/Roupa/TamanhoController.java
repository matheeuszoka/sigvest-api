package br.com.sigvest.api.controller.Roupa;

import br.com.sigvest.api.model.produto.Roupa.Marca;
import br.com.sigvest.api.model.produto.Roupa.Tamanho;
import br.com.sigvest.api.service.RoupaSKUService.TamanhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tamanho")
public class TamanhoController {

    @Autowired
    private TamanhoService tamanhoService;

    @GetMapping
    public List<Tamanho> listar() {
        return tamanhoService.listar();
    }

    @PostMapping
    public Tamanho setMarca(@RequestBody Tamanho tamanho) {
        return tamanhoService.salvar(tamanho);
    }

}
