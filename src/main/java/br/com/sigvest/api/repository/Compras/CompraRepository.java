package br.com.sigvest.api.repository.Compras;

import br.com.sigvest.api.model.compras.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    // CORRIGIDO: c.itemCompras → c.itens
    @Query("SELECT c FROM Compra c " +
            "LEFT JOIN FETCH c.itens ic " +
            "LEFT JOIN FETCH ic.derivacao d " +
            "LEFT JOIN FETCH d.produto p " +
            "LEFT JOIN FETCH c.movimentarFinanceiro mf " +
            "LEFT JOIN FETCH c.fornecedor f " +
            "WHERE c.dataCompra BETWEEN :inicio AND :fim")
    List<Compra> findComprasComDetalhesPorPeriodo(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("""
        SELECT c FROM Compra c
        LEFT JOIN FETCH c.fornecedor f
        LEFT JOIN FETCH c.movimentarFinanceiro mf
        LEFT JOIN FETCH mf.planoPagamento pp
        LEFT JOIN FETCH pp.contaCorrente cc
        LEFT JOIN FETCH c.itens ic
        LEFT JOIN FETCH ic.derivacao d
        LEFT JOIN FETCH d.produto p
        WHERE c.idCompra = :id
    """)
    Optional<Compra> findByIdComDetalhes(@Param("id") Long id);

    @Query("SELECT COALESCE(SUM(c.totalCompra), 0) FROM Compra c " +
            "WHERE c.dataCompra BETWEEN :inicio AND :fim")
    BigDecimal findTotalComprasPorPeriodo(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("""
  SELECT DISTINCT c FROM Compra c
  LEFT JOIN FETCH c.fornecedor f
  LEFT JOIN FETCH c.movimentarFinanceiro mf
  LEFT JOIN FETCH mf.planoPagamento pp
  LEFT JOIN FETCH pp.contaCorrente cc
  LEFT JOIN FETCH c.itens ic
  LEFT JOIN FETCH ic.derivacao d
  LEFT JOIN FETCH d.produto p
  LEFT JOIN FETCH p.marca m
""")
    List<Compra> findAllComDetalhes();


    List<Compra> findByFornecedorIdFornecedor(Long idFornecedor);

    List<Compra> findByNumeroNota(String numeroNota);
}
