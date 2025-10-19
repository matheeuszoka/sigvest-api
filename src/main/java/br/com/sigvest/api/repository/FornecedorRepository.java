package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {


    @Query(value = "select * from fornecedor where razao_social like %:razaoSocial%", nativeQuery = true)
    List<Fornecedor> buscarLikeFornecedor(@Param("razaoSocial") String razaoSocial);
}
