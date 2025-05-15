package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.pessoa.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PessoaRepository extends JpaRepository<Pessoa,Long> {

    @Query(value = "select * from pessoa where name_completo like %:nomeCompleto%", nativeQuery = true)
    List<Pessoa> buscarLikeNome(@Param("nomeCompleto") String nomeCompleto);

}

