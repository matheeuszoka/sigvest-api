package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query(value = "SELECT p.* FROM produto p " +
            "JOIN marca m ON p.marca_id = m.id " +
            "WHERE p.nome LIKE CONCAT('%', :termo, '%') " +
            "OR p.codigo LIKE CONCAT('%', :termo, '%') " +
            "OR m.marca LIKE CONCAT('%', :termo, '%')",
            nativeQuery = true)
    List<Produto> buscarProdutos(@Param("termo") String termo);


}
