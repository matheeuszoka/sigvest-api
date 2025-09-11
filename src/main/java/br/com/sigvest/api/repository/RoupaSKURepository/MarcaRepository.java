package br.com.sigvest.api.repository.RoupaSKURepository;

import br.com.sigvest.api.model.produto.Roupa.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    Optional<Marca> findByMarca(String marca);

    @Query(value = "select * from marca where marca like %:marca%", nativeQuery = true)
    List<Marca> buscarLikeMarca(@Param("marca") String marca);
}
