package br.com.sigvest.api.service;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.endereco.Estado;
import br.com.sigvest.api.model.funcionario.Cargo;
import br.com.sigvest.api.model.funcionario.Funcionario;
import br.com.sigvest.api.model.produto.Roupa.Marca;
import br.com.sigvest.api.repository.CargoRepository;
import br.com.sigvest.api.repository.EnderecoRepository.CidadeRepository;
import br.com.sigvest.api.repository.EnderecoRepository.EstadoRepository;
import br.com.sigvest.api.repository.FuncionarioRepository;
import br.com.sigvest.api.service.EnderecoService.EnderecoHierarquiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    public Cargo salvar(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    public List<Cargo> listar() {
        return cargoRepository.findAll();
    }

    public List<Cargo> buscarLikeCargo(String nomeCargo) {
        return cargoRepository.buscarLikeCargo(nomeCargo);
    }


    public boolean deletar(Long id) {
        Optional<Cargo> cargoOptional = cargoRepository.findById(id);
        if (cargoOptional.isPresent()) {
            cargoRepository.delete(cargoOptional.get());
            return true;
        } else {
            return false;
        }
    }


    public Cargo atualizarCargo(Long id, Cargo cargoAtualizado) {
        Cargo cargoExistente = cargoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cargo não encontrado"));

        cargoExistente.setNomeCargo(cargoAtualizado.getNomeCargo());
        cargoExistente.setSalarioBruto(cargoAtualizado.getSalarioBruto());
        cargoExistente.setDesconto(cargoAtualizado.getDesconto());
        cargoExistente.setSalarioLiquido(cargoAtualizado.getSalarioLiquido());
        cargoExistente.setStatus(cargoAtualizado.getStatus());
        return cargoRepository.save(cargoExistente);
    }

    public Optional<Cargo> buscarPorId(Long id) {
        return cargoRepository.findById(id);
    }
}