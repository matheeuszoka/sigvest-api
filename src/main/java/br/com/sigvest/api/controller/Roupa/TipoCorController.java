package br.com.sigvest.api.controller.Roupa;

import br.com.sigvest.api.model.produto.Roupa.Marca;
import br.com.sigvest.api.model.produto.Roupa.tipoCor;
import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
import br.com.sigvest.api.service.RoupaSKUService.TipoRoupaService;
import br.com.sigvest.api.service.RoupaSKUService.tipoCorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tipocor")
public class TipoCorController {

    @Autowired
    private tipoCorService tipoCorService;

    @GetMapping
    public List<tipoCor> listar() {
        return tipoCorService.listar();
    }

    @PostMapping
    public tipoCor setMarca(@RequestBody tipoCor tipoCor) {
        return tipoCorService.salvar(tipoCor);
    }

}
