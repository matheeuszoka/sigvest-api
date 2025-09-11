package br.com.sigvest.api.repository.RoupaSKURepository;

import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface tipoRoupaRepository extends JpaRepository<tipoRoupa, Long> {
    Optional<tipoRoupa> findByTipoRoupa(String tipoRoupa);
}
