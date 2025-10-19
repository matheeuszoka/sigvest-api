package br.com.sigvest.api.controller.Roupa;

import br.com.sigvest.api.model.produto.Roupa.Marca;
import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
import br.com.sigvest.api.service.RoupaSKUService.TipoRoupaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tiporoupa")
public class TipoRoupaController {

    @Autowired
    private TipoRoupaService tipoRoupaService;

    @GetMapping
    public List<tipoRoupa> listar() {
        return tipoRoupaService.listar();
    }

    @PostMapping
    public tipoRoupa setMarca(@RequestBody tipoRoupa tipoRoupa) {
        return tipoRoupaService.salvar(tipoRoupa);
    }

}
