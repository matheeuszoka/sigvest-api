package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.endereco.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {


}
