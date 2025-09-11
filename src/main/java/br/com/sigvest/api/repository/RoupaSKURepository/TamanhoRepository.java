package br.com.sigvest.api.repository.RoupaSKURepository;

import br.com.sigvest.api.model.produto.Roupa.Tamanho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TamanhoRepository extends JpaRepository<Tamanho, Long> {
    Optional<Tamanho> findByNomeTamanho(String nomeTamanho);
}

