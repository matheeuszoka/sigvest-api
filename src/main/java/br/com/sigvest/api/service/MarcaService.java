package br.com.sigvest.api.service;

import br.com.sigvest.api.model.endereco.Estado;
import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.model.produto.Marca;
import br.com.sigvest.api.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    public Marca salvar(Marca marca
    ){
        return marcaRepository.save(marca);
    }

    public List<Marca> listar(){
        return marcaRepository.findAll();
    }

    public boolean deletar(Long id) {
        Optional<Marca> marcaOptional = marcaRepository.findById(id);
        if (marcaOptional.isPresent()) {
            marcaRepository.delete(marcaOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public Marca atualizarMarca(Long id, Marca marcaAtualizado) {
        Marca marcaExistente = marcaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("marca não encontrada"));

        marcaExistente.setMarca(marcaAtualizado.getMarca());
        return marcaRepository.save(marcaExistente);
    }

}
