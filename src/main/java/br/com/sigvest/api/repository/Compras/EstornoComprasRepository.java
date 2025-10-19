package br.com.sigvest.api.repository.Compras;

import br.com.sigvest.api.model.compras.EstornoCompras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstornoComprasRepository extends JpaRepository<EstornoCompras, Long> {
}