package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.endereco.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    @Query("SELECT e FROM Estado e WHERE UPPER(e.uf) = UPPER(:uf) OR UPPER(e.nomeEstado) = UPPER(:nomeEstado)")
    Optional<Estado> findByUfOrNomeEstadoIgnoreCase(@Param("uf") String uf, @Param("nomeEstado") String nomeEstado);

}
