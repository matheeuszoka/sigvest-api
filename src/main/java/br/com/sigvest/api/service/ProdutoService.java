package br.com.sigvest.api.service;

import br.com.sigvest.api.model.produto.Marca;
import br.com.sigvest.api.model.produto.Produto;
import br.com.sigvest.api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto salvar(Produto produto){
        return produtoRepository.save(produto);
    }

    public List<Produto> listar(){
        return produtoRepository.findAll();
    }

    public boolean deletar(Long id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        if (produtoOptional.isPresent()) {
            produtoRepository.delete(produtoOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrada"));

        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setCodigo(produtoAtualizado.getCodigo());
        produtoExistente.setEstoque(produtoAtualizado.getEstoque());
        produtoExistente.setPrecoCusto(produtoAtualizado.getPrecoCusto());
        produtoExistente.setPrecoVenda(produtoAtualizado.getPrecoVenda());
        produtoExistente.setMarca(produtoAtualizado.getMarca());
        produtoExistente.setTamanho(produtoAtualizado.getTamanho());
        produtoExistente.setTipoRoupa(produtoAtualizado.getTipoRoupa());

        return produtoRepository.save(produtoExistente);
    }

}
