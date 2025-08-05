package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.model.endereco.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {


    @Query(value = "select * from cidade where nome_cidade like %:nomeCidade%", nativeQuery = true)
    List<Cidade> buscarLikeCidade(@Param("nomeCidade") String nomeCidade);

    @Query("SELECT c FROM Cidade c WHERE UPPER(c.nomeCidade) = UPPER(:nomeCidade)")
    Optional<Cidade> findByCidade(@Param("nomeCidade") String nomeCidade);

}
