package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
