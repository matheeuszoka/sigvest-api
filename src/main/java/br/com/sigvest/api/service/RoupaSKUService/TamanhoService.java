package br.com.sigvest.api.service.RoupaSKUService;

import br.com.sigvest.api.model.produto.Roupa.Marca;
import br.com.sigvest.api.model.produto.Roupa.Tamanho;
import br.com.sigvest.api.repository.RoupaSKURepository.TamanhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TamanhoService {

    @Autowired
    private TamanhoRepository tamanhoRepository;

    public Tamanho salvar(Tamanho tamanho) {
        return tamanhoRepository.save(tamanho);
    }

    public List<Tamanho> listar() {
        return tamanhoRepository.findAll();
    }

    public boolean deletar(Long id) {
        Optional<Tamanho> tamanhoOptional = tamanhoRepository.findById(id);
        if (tamanhoOptional.isPresent()) {
            tamanhoRepository.delete(tamanhoOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public Tamanho atualizarTamanho(Long id, Tamanho tamanhoAtualizado) {
        Tamanho tamanhoExistente = tamanhoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tamanho não encontrado"));

        tamanhoExistente.setNomeTamanho(tamanhoAtualizado.getNomeTamanho());
        return tamanhoRepository.save(tamanhoExistente);
    }

    public Optional<Tamanho> buscarPorId(Long id) {
        return tamanhoRepository.findById(id);
    }

    public Tamanho buscarOuCriar(String nomeTamanho) {
        return tamanhoRepository.findByNomeTamanho(nomeTamanho)
                .orElseGet(() -> {
                    Tamanho novoTamanho = new Tamanho();
                    novoTamanho.setNomeTamanho(nomeTamanho);
                    return tamanhoRepository.save(novoTamanho);
                });
    }

}
