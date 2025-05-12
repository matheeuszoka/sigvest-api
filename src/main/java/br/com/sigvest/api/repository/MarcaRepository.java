package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.produto.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
}
