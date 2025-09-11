package br.com.sigvest.api.repository.RoupaSKURepository;

import br.com.sigvest.api.model.produto.Roupa.tipoCor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface tipoCorRepository extends JpaRepository<tipoCor, Long> {
    Optional<tipoCor> findByNomeCor(String nomeCor);
}
