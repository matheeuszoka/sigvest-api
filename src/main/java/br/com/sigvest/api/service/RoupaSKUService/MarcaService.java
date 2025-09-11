package br.com.sigvest.api.service.RoupaSKUService;

import br.com.sigvest.api.model.produto.Roupa.Marca;
import br.com.sigvest.api.repository.RoupaSKURepository.MarcaRepository;
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
        marcaExistente.setDesMarca(marcaAtualizado.getDesMarca());
        marcaExistente.setStatus(marcaAtualizado.getStatus());
        return marcaRepository.save(marcaExistente);
    }
    public Optional<Marca> buscarPorId(Long id) {
        return marcaRepository.findById(id);
    }

    public List<Marca> buscarLikeMarca(String marca) {
        return marcaRepository.buscarLikeMarca(marca);
    }

    public Marca buscarOuCriar(String nomeMarca) {
        return marcaRepository.findByMarca(nomeMarca)
                .orElseGet(() -> {
                    Marca novaMarca = new Marca();
                    novaMarca.setMarca(nomeMarca);
                    return marcaRepository.save(novaMarca);
                });
    }

}
