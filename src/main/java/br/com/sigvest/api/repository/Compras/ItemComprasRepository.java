package br.com.sigvest.api.repository.Compras;

import br.com.sigvest.api.model.compras.ItemCompras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemComprasRepository extends JpaRepository<ItemCompras, Long> {

    // CORRIGIDO: Caminho correto através de derivacao.produto.idProduto
    List<ItemCompras> findByDerivacaoProdutoIdProduto(Long idProduto);

    // Método alternativo usando @Query para maior clareza
    @Query("SELECT ic FROM ItemCompras ic " +
            "JOIN ic.derivacao d " +
            "JOIN d.produto p " +
            "WHERE p.idProduto = :idProduto")
    List<ItemCompras> findByProdutoId(@Param("idProduto") Long idProduto);

    // Buscar itens por compra
    List<ItemCompras> findByCompraIdCompra(Long idCompra);

    // Buscar itens por SKU (derivação)
    List<ItemCompras> findByDerivacaoIdDerivacao(Long idDerivacao);

    // Buscar itens por código SKU
    @Query("SELECT ic FROM ItemCompras ic " +
            "JOIN ic.derivacao d " +
            "WHERE d.codigoSKU = :codigoSKU")
    List<ItemCompras> findByCodigoSKU(@Param("codigoSKU") String codigoSKU);

    @Query("SELECT ic FROM ItemCompras ic JOIN FETCH ic.derivacao d WHERE ic.idICompras = :idItem")
    Optional<ItemCompras> findByIdComDerivacao(@Param("idItem") Long idItem);
}
