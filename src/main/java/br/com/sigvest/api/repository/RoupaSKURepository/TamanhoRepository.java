package br.com.sigvest.api.repository.RoupaSKURepository;

import br.com.sigvest.api.model.produto.Roupa.Tamanho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TamanhoRepository extends JpaRepository<Tamanho, Long> {
}
