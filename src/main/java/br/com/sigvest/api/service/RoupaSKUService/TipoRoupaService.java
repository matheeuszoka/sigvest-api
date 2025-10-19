package br.com.sigvest.api.service.RoupaSKUService;


import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.model.produto.Roupa.tipoCor;
import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
import br.com.sigvest.api.repository.RoupaSKURepository.tipoRoupaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<tipoRoupa> listar() {
        return tipoRoupaRepository.findAll();
    }

    public tipoRoupa salvar(tipoRoupa tipoRoupa) {
        return tipoRoupaRepository.save(tipoRoupa);
    }

}
