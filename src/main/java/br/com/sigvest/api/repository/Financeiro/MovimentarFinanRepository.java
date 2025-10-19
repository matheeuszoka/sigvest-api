package br.com.sigvest.api.repository.Financeiro;

import br.com.sigvest.api.model.financeiro.MovimentarFinanceiro;
import br.com.sigvest.api.model.financeiro.StatusFinanceiro;
import br.com.sigvest.api.model.financeiro.TipoFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface MovimentarFinanRepository extends JpaRepository<MovimentarFinanceiro, Long> {
    List<MovimentarFinanceiro> findByStatus(StatusFinanceiro status);
    List<MovimentarFinanceiro> findByDataLancamentoBetween(Date inicio, Date fim);
    List<MovimentarFinanceiro> findByTipoFinanceiro(TipoFinanceiro tipo);

    @Query("SELECT SUM(m.valor) FROM MovimentarFinanceiro m WHERE m.status = :status AND m.tipoFinanceiro = :tipo")
    BigDecimal calcularTotalPorTipoEStatus(@Param("tipo") TipoFinanceiro tipo, @Param("status") StatusFinanceiro status);
}
