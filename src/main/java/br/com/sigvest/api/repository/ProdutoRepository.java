package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.produto.Derivacao;
import br.com.sigvest.api.model.produto.Produto;
import br.com.sigvest.api.model.produto.Roupa.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Produto “pai” único por nome + marca
    Optional<Produto> findByNomeProdutoAndMarca(String nomeProduto, Marca marca);

    // Busca aproximada por nome
    List<Produto> findByNomeProdutoContainingIgnoreCase(String nomeProduto);

    // Encontra o produto a partir de uma derivação (no modelo 1:N a Derivacao aponta para Produto)
    Optional<Produto> findByDerivacoesContains(Derivacao derivacao);

    // Caso precise usar a forma “por derivacao” em vez de contains:
    // Optional<Produto> findByDerivacoes_IdDerivacao(Long idDerivacao);

    // Consulta nativa de busca por termo (nome do produto e marca)
    @Query(value = "SELECT p.* FROM produto p " +
            "JOIN marca m ON p.id_marca = m.id_marca " +
            "WHERE p.nome_produto LIKE CONCAT('%', :termo, '%') " +
            "   OR m.marca LIKE CONCAT('%', :termo, '%')",
            nativeQuery = true)
    List<Produto> buscarProdutos(@Param("termo") String termo);
}
