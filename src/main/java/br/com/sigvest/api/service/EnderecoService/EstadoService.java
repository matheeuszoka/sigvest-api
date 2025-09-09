package br.com.sigvest.api.service.EnderecoService;

import br.com.sigvest.api.model.endereco.Estado;
import br.com.sigvest.api.repository.EnderecoRepository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoService {


    @Autowired
    private EstadoRepository estadoRepository;


    public Estado ObterOuCriarEstado(Estado estado) {
        if (estado == null){
            throw new IllegalArgumentException("EstadoNulo");
        }

        validarEstado(estado);

        Optional<Estado> estadoExistente = buscarEstadoExistente(estado);

        if (estadoExistente.isPresent()) {
            return estadoExistente.get();
        } else {
            return estadoRepository.save(estado);
        }
    }


    private void validarEstado(Estado estado) {
        if (estado.getUf() == null || estado.getUf().trim().isEmpty()) {
            throw new IllegalArgumentException("UF nulo");
        }
        if (estado.getNomeEstado() == null || estado.getNomeEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("Estado vazio");
        }
    }



    private Optional<Estado> buscarEstadoExistente(Estado estado) {
        return estadoRepository.findByUfOrNomeEstadoIgnoreCase(estado.getUf(), estado.getNomeEstado());

    }

    public Estado salvar(Estado estado) {
        return estadoRepository.save(estado);
    }

    public List<Estado> listar() {
        return estadoRepository.findAll();
    }
}
