package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.produto.Produto;
import br.com.sigvest.api.model.produto.Roupa.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Produto "pai" único por nome + marca
    Optional<Produto> findByNomeProdutoAndMarca(String nomeProduto, Marca marca);

    // Busca aproximada por nome
    List<Produto> findByNomeProdutoContainingIgnoreCase(String nomeProduto);

    // Consulta nativa de busca por termo (nome do produto e marca)
    @Query(value = "SELECT p.* FROM produto p " +
            "JOIN marca m ON p.id_marca = m.id_marca " +
            "WHERE p.nome_produto LIKE CONCAT('%', :termo, '%') " +
            "   OR m.marca LIKE CONCAT('%', :termo, '%')",
            nativeQuery = true)
    List<Produto> buscarProdutos(@Param("termo") String termo);

    // Buscar produtos com estoque (via derivações)
    @Query("SELECT DISTINCT p FROM Produto p JOIN p.derivacoes d WHERE d.estoque > 0")
    List<Produto> findProdutosComEstoque();

    // Contar derivações de um produto
    @Query("SELECT COUNT(d) FROM Derivacao d WHERE d.produto = :produto")
    Long countDerivacoesByProduto(@Param("produto") Produto produto);

    @Query("SELECT p.id, CONCAT(p.nomeProduto, ' ', m.marca) " +
            "FROM Produto p " +
            "INNER JOIN p.marca m")
    List<String> findAllProdutosComMarca();

    @Query("""
    SELECT DISTINCT p FROM Produto p
    LEFT JOIN FETCH p.marca m
    LEFT JOIN FETCH p.derivacoes d
    LEFT JOIN FETCH d.tipoRoupa tr
    LEFT JOIN FETCH d.tipoCor tc
    LEFT JOIN FETCH d.tamanho tm
  """)
    List<Produto> findAllComDerivacoes();

    @Query("""
    SELECT p FROM Produto p
    LEFT JOIN FETCH p.marca m
    LEFT JOIN FETCH p.derivacoes d
    LEFT JOIN FETCH d.tipoRoupa tr
    LEFT JOIN FETCH d.tipoCor tc
    LEFT JOIN FETCH d.tamanho tm
    WHERE p.idProduto = :id
  """)
    Optional<Produto> findByIdComDerivacoes(@Param("id") Long id);
}



