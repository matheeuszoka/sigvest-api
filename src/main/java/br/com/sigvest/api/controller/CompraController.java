package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.compras.Compra;
import br.com.sigvest.api.model.compras.dto.EstornoCompraRequest;
import br.com.sigvest.api.service.CompraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/compras", "/api/compras"})
@Slf4j
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Compra> criarCompra(@RequestBody Compra compra) {
        try {
            Compra criada = compraService.processarCompraCompleta(compra);
            return ResponseEntity.status(HttpStatus.CREATED).body(criada);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao criar compra: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Erro interno ao criar compra", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> buscarPorId(@PathVariable Long id) {
        return compraService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Compra>> listar() {
        List<Compra> compras = compraService.listar();
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Compra>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<Compra> compras = compraService.buscarComprasPorPeriodo(inicio, fim);
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/total-periodo")
    public ResponseEntity<BigDecimal> totalPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        BigDecimal total = compraService.calcularTotalComprasPorPeriodo(inicio, fim);
        return ResponseEntity.ok(total);
    }

    @PostMapping("/estorno") // Mapeia para o URL usado no frontend: `${apiBase}/compras/estorno`
    public ResponseEntity<Void> estornarCompra(@RequestBody EstornoCompraRequest request) {
        log.info("Recebida requisição de estorno para Compra ID: {}", request.getIdCompra());
        try {
            compraService.estornarCompra(request);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação/regra de negócio no estorno: {}", e.getMessage());
            // Retorna a mensagem de erro no corpo da resposta 400
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            log.warn("Erro de estado/regra de negócio no estorno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error("Erro interno ao processar estorno de compra", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
