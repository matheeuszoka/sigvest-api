package br.com.sigvest.api.service;

import br.com.sigvest.api.model.endereco.Estado;
import br.com.sigvest.api.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService {


    @Autowired
    private EstadoRepository estadoRepository;

    public Estado salvar(Estado estado){
        return estadoRepository.save(estado);
    }

    public List<Estado> listar(){
        return estadoRepository.findAll();
    }
}
