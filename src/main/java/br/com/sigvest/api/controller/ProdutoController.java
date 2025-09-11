package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.produto.Produto;
import br.com.sigvest.api.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Produto produto) {
        try {
            return ResponseEntity.ok(produtoService.salvarProduto(produto));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    public List<Produto> listar() {
        return produtoService.listar();
    }


    @GetMapping("/sku/{sku}")
    public ResponseEntity<Produto> buscarPorSKU(@PathVariable String sku) {
        Optional<Produto> produto = produtoService.buscarPorSKU(sku);
        return produto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }



    @GetMapping("/{id}")
    public Optional<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        boolean deletado = produtoService.deletar(id);
        if (deletado) {
            return ResponseEntity.ok("Produto deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
    }
}
