package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.produto.Roupa.Marca;
import br.com.sigvest.api.service.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("marca")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;


    @GetMapping
    public List<Marca> listar() {
        return marcaService.listar();
    }
    @PostMapping
    public Marca setMarca(@RequestBody Marca marca) {
        return marcaService.salvar(marca);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marca> atualizarMarca(@PathVariable Long id, @RequestBody Marca marca){
        Marca marcaUp = marcaService.atualizarMarca(id, marca);
        return ResponseEntity.ok(marcaUp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        boolean deletado = marcaService.deletar(id);
        if (deletado) {
            return ResponseEntity.ok("Marca deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Marca não encontrado.");
        }
    }
    @GetMapping("/{id}")
    public Optional<Marca> buscarPorId(@PathVariable Long id) {
        return marcaService.buscarPorId(id);
    }

    @GetMapping("/likemarca/{marca}")
    public List<Marca> buscarLikeMarca(@PathVariable String marca){
        return marcaService.buscarLikeMarca(marca);
    }
}
