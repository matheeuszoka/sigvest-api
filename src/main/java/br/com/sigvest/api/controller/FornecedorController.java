package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.fornecedor.Fornecedor;
import br.com.sigvest.api.model.pessoa.Pessoa;
import br.com.sigvest.api.service.EnderecoHierarquiaService;
import br.com.sigvest.api.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private EnderecoHierarquiaService enderecoHierarquiaService;

    @PostMapping
    @Transactional
    public ResponseEntity<Fornecedor> criarFornecedor(@RequestBody Fornecedor fornecedor) {
        try {
            if (fornecedor.getEndereco() != null) {
                Endereco enderecoProcessado = enderecoHierarquiaService
                        .processarHierarquiaEndereco(fornecedor.getEndereco());
                fornecedor.setEndereco(enderecoProcessado);
            }

            Fornecedor fornecedorSalvo = fornecedorService.salvar(fornecedor);

            return ResponseEntity.ok(fornecedorSalvo);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
