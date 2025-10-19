package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.funcionario.Cargo;
import br.com.sigvest.api.model.produto.Roupa.Marca;
import br.com.sigvest.api.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("cargos")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @GetMapping
    public List<Cargo> listarCargos() {
        return cargoService.listar();
    }

    @PostMapping
    public Cargo setCargo(@RequestBody Cargo cargo) {
        return cargoService.salvar(cargo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCargo(@PathVariable Long id) {
        boolean deletado = cargoService.deletar(id);

        if (deletado) {
            return ResponseEntity.ok("Cargo deletado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cargo não encontrado");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCargo(
            @PathVariable Long id,
            @RequestBody Cargo cargoAtualizado) {
        try {
            Cargo cargoAtualizadoR = cargoService.atualizarCargo(id, cargoAtualizado);
            return ResponseEntity.ok(cargoAtualizadoR);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erro", "Erro interno do servidor"));
        }

    }

    @GetMapping("/{id}")
    public Optional<Cargo> buscarPorId(@PathVariable Long id) {
        return cargoService.buscarPorId(id);
    }

    @GetMapping("/likecargo/{cargo}")
    public List<Cargo> buscarLikeCargo(@PathVariable String cargo){
        return cargoService.buscarLikeCargo(cargo);
    }
}
