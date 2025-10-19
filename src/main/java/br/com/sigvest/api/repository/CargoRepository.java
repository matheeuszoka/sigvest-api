package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.funcionario.Cargo;
import br.com.sigvest.api.model.funcionario.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CargoRepository extends JpaRepository<Cargo,Long> {

    @Query(value = "select * from cargos where nome_cargo like %:nomeCargo%", nativeQuery = true)
    List<Cargo> buscarLikeCargo(@Param("nomeCargo") String nomeCargo);


}

