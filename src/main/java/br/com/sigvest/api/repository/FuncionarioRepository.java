// FuncionarioRepository.java
package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.funcionario.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    @Query(value = """
        SELECT * FROM funcionario WHERE nome_completo ILIKE %:nomeCompleto%
    """, nativeQuery = true)
    List<Funcionario> buscarLikeNome(@Param("nomeCompleto") String nomeCompleto);
}
