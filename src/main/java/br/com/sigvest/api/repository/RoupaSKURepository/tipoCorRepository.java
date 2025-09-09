package br.com.sigvest.api.repository.RoupaSKURepository;

import br.com.sigvest.api.model.produto.Roupa.tipoCor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface tipoCorRepository extends JpaRepository<tipoCor, Long> {
}
