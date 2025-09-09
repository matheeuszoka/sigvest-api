package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.service.EnderecoService.CidadeService;
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
    public Cidade setCidade(@RequestBody Cidade cidade) {
        return cidadeService.salvar(cidade);
    }

    @GetMapping("/likecidade/{nomeCidade}")
    public List<Cidade> buscarLikeCidade(@PathVariable String nomeCidade){
        return cidadeService.buscarLikeCidade(nomeCidade);
    }

}
