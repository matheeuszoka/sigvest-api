package br.com.sigvest.api.service.RoupaSKUService;

import br.com.sigvest.api.model.produto.Roupa.Tamanho;
import br.com.sigvest.api.model.produto.Roupa.tipoCor;
import br.com.sigvest.api.repository.RoupaSKURepository.tipoCorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class tipoCorService {

    @Autowired
    private tipoCorRepository tipoCorRepository;

    public tipoCor salvar(tipoCor tipoCor) {
        return tipoCorRepository.save(tipoCor);
    }

    public List<tipoCor> listar() {
        return tipoCorRepository.findAll();
    }

    public boolean deletar(Long id) {
        Optional<tipoCor> corOptional = tipoCorRepository.findById(id);
        if (corOptional.isPresent()) {
            tipoCorRepository.delete(corOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public tipoCor atualizarTamanho(Long id, tipoCor corAtualizado) {
        tipoCor corExistente = tipoCorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cor não encontrada"));

        corExistente.setNomeCor(corAtualizado.getNomeCor());
        return tipoCorRepository.save(corExistente);
    }

    public Optional<tipoCor> buscarPorId(Long id) {
        return tipoCorRepository.findById(id);
    }

    public tipoCor buscarOuCriar(String nomeCor) {
        return tipoCorRepository.findByNomeCor(nomeCor)
                .orElseGet(() -> {
                    tipoCor novaCor = new tipoCor();
                    novaCor.setNomeCor(nomeCor);
                    return tipoCorRepository.save(novaCor);
                });
    }

}
