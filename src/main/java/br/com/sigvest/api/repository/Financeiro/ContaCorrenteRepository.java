package br.com.sigvest.api.repository.Financeiro;

import br.com.sigvest.api.model.financeiro.ContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, Long> {
    Optional<ContaCorrente> findByNumeroConta(String numeroConta);
    Optional<ContaCorrente> findByBancoAndAgencia(String banco, String agencia);

    @Query("SELECT SUM(c.saldoAtual) FROM ContaCorrente c")
    BigDecimal calcularSaldoTotalContas();
}