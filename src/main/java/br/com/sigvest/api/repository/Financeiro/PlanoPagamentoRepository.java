package br.com.sigvest.api.repository.Financeiro;

import br.com.sigvest.api.model.financeiro.PlanoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanoPagamentoRepository extends JpaRepository<PlanoPagamento, Long> {
}
