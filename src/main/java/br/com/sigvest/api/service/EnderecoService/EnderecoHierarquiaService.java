package br.com.sigvest.api.service.EnderecoService;

import br.com.sigvest.api.model.endereco.Cidade;
import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.endereco.Estado;
import br.com.sigvest.api.repository.EnderecoRepository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EnderecoHierarquiaService {

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private CidadeService cidadeService;

    @Autowired
    private EnderecoRepository enderecoRepository;


    public Endereco processarHierarquiaEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereco não pode ser nulo");
        }

        Estado estado = processarEstado(endereco.getCidade().getEstado());

        Cidade cidade = processarCidade(endereco.getCidade(), estado);

        endereco.setCidade(cidade);

        return endereco;
    }

    private Estado processarEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Estado não pode ser nulo");
        }
        return estadoService.ObterOuCriarEstado(estado);
    }

    private Cidade processarCidade(Cidade cidade, Estado estado) {
        if (cidade == null) {
            throw new IllegalArgumentException("Cidade não pode ser nula");
        }

        cidade.setEstado(estado);
        return cidadeService.ObterOuCriarCidade(cidade);
    }
}
