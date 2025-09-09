package br.com.sigvest.api.repository.EnderecoRepository;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.model.endereco.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    @Query("SELECT e FROM Endereco e WHERE " +
            "e.logradouro = :logradouro AND " +
            "e.numero = :numero AND " +
            "e.cep = :cep AND " +
            "e.cidade = :cidade")
    Optional<Endereco> findByLogradouroAndNumeroAndCepAndCidade(
            @Param("logradouro") String logradouro,
            @Param("numero") String numero,
            @Param("cep") String cep,
            @Param("cidade") Cidade cidade
    );
}


