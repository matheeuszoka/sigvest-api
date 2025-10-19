package br.com.sigvest.api.controller;

import br.com.sigvest.api.model.financeiro.PlanoPagamento;
import br.com.sigvest.api.model.funcionario.Cargo;
import br.com.sigvest.api.service.PlanoPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("plano-pagamento")
public class PlanoPagamentoController {

    @Autowired
    private PlanoPagamentoService planoPagamentoService;

    @GetMapping
    public List<PlanoPagamento> listarPlanos() {
        return planoPagamentoService.listar();
    }

    @PostMapping
    public PlanoPagamento setPlano(@RequestBody PlanoPagamento planoPagamento) {
        return planoPagamentoService.salvar(planoPagamento);
    }

}
