package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.pessoa.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa,Long> {

}
