package br.com.sigvest.api.repository.RoupaSKURepository;

import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface tipoRoupaRepository extends JpaRepository<tipoRoupa, Long> {
}
