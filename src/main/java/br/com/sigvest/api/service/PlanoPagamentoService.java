package br.com.sigvest.api.service;


import br.com.sigvest.api.model.financeiro.PlanoPagamento;
import br.com.sigvest.api.repository.Financeiro.PlanoPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanoPagamentoService {

    @Autowired
    private PlanoPagamentoRepository planoPagamentoRepository;


    public PlanoPagamento salvar(PlanoPagamento planoPagamento) {
        return planoPagamentoRepository.save(planoPagamento);
    }

    public List<PlanoPagamento> listar() {
        return planoPagamentoRepository.findAll();
    }

}
