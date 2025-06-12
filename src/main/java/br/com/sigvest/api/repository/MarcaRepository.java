package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.model.produto.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    @Query(value = "select * from marca where marca like %:marca%", nativeQuery = true)
    List<Marca> buscarLikeMarca(@Param("marca") String marca);
}
