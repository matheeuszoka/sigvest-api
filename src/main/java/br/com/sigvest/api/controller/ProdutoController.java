package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.produto.Marca;
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

    @GetMapping
    public List<Produto> listar() {
        return produtoService.listar();
    }

    @PostMapping
    public Produto setProduto(@RequestBody Produto produto) {
        return produtoService.salvarProduto(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto){
        Produto produtoUp = produtoService.atualizarProduto(id, produto);
        return ResponseEntity.ok(produtoUp);
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

    // Endpoint adicionado para buscar produto por ID
    @GetMapping("/{id}")
    public Optional<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    // Endpoint para buscar produtos por nome (assumindo que você tem esse método no service)
//    @GetMapping("/likeproduto/{nome}")
//    public List<Produto> buscarLikeNome(@PathVariable String nome){
//        return produtoService.buscarLikeNome(nome);
//    }
}