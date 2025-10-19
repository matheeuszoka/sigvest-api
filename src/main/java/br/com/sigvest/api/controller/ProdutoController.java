package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.produto.Derivacao;
import br.com.sigvest.api.model.produto.Produto;
import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
import br.com.sigvest.api.service.ProdutoService;
import br.com.sigvest.api.service.RoupaSKUService.TipoRoupaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("produto")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;



    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Produto produto) {
        try {
            Produto produtoSalvo = produtoService.salvarProduto(produto);
            return ResponseEntity.ok(produtoSalvo);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + ex.getMessage());
        }
    }

    @GetMapping
    public List<Produto> listar() {
        return produtoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.buscarPorId(id);
        return produto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<Produto> buscarPorSKU(@PathVariable String sku) {
        Optional<Produto> produto = produtoService.buscarPorSKU(sku);
        return produto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/derivacao/produto/{idProduto}")
    public ResponseEntity<List<Derivacao>> buscarDerivacoesPorProduto(@PathVariable Long idProduto) {
        List<Derivacao> derivacoes = produtoService.buscarDerivacoesPorProduto(idProduto);
        if (derivacoes == null || derivacoes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(derivacoes);
    }

    @GetMapping("/buscar/produtos-marcas")
    public List<String>BuscartodosMarcaProduto(){
        return produtoService.findAllporprodutomarca();
    }


//    @GetMapping("/estoque")
//    public List<Produto> listarComEstoque() {
//        return produtoService.listarProdutosComEstoque();
//    }

    @GetMapping("/buscar")
    public List<Produto> buscar(@RequestParam String termo) {
        return produtoService.buscarProdutos(termo);
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

    @PutMapping("/derivacao/{idDerivacao}/estoque")
    public ResponseEntity<?> atualizarEstoque(
            @PathVariable Long idDerivacao,
            @RequestParam Integer estoque) {
        try {
            return ResponseEntity.ok(produtoService.atualizarEstoque(idDerivacao, estoque));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
