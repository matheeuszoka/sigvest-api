package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
}
