package br.com.sigvest.api.service;

import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.model.produto.Marca;
import br.com.sigvest.api.model.produto.Produto;
import br.com.sigvest.api.repository.ProdutoRepository;
import br.com.sigvest.api.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto salvarProduto(Produto produto) {
        if (produto.getMarca() == null) {
            throw new IllegalArgumentException("Marca é obrigatória");
        }
        // Salvar o produto no banco de dados
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
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setCodigo(produtoAtualizado.getCodigo());
        produtoExistente.setEstoque(produtoAtualizado.getEstoque());
        produtoExistente.setPrecoCusto(produtoAtualizado.getPrecoCusto());
        produtoExistente.setPrecoVenda(produtoAtualizado.getPrecoVenda());
        produtoExistente.setTamanho(produtoAtualizado.getTamanho());
        produtoExistente.setTipoRoupa(produtoAtualizado.getTipoRoupa());

        // Se uma marca foi informada, criar apenas a referência com o ID
        if (produtoAtualizado.getMarca() != null && produtoAtualizado.getMarca().getIdMarca() != null) {
            Marca marcaRef = new Marca();
            marcaRef.setIdMarca(produtoAtualizado.getMarca().getIdMarca());
            produtoExistente.setMarca(marcaRef);
        } else {
            produtoExistente.setMarca(null);
        }

        return produtoRepository.save(produtoExistente);
    }

    // Método adicionado para buscar produto por ID
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }


    // Método adicionado para buscar produtos por nome (você precisa implementar no repository)
//    public List<Produto> buscarLikeNome(String nome) {
//        return produtoRepository.buscarLikeNome(nome);
//    }
}