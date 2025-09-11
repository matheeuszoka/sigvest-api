package br.com.sigvest.api.service.RoupaSKUService;


import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
import br.com.sigvest.api.repository.RoupaSKURepository.tipoRoupaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TipoRoupaService {

    @Autowired
    private tipoRoupaRepository tipoRoupaRepository;

    public tipoRoupa buscarPorNome(String nome) {
        return tipoRoupaRepository.findByTipoRoupa(nome)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de roupa não encontrado: " + nome));
    }

    public tipoRoupa buscarOuCriar(String nome) {
        return tipoRoupaRepository.findByTipoRoupa(nome)
                .orElseGet(() -> {
                    tipoRoupa novoTipo = new tipoRoupa();
                    novoTipo.setTipoRoupa(nome);
                    return tipoRoupaRepository.save(novoTipo);
                });
    }
}
