package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.produto.Derivacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DerivacaoRepository extends JpaRepository<Derivacao, Long> {
}
