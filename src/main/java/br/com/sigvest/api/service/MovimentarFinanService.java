package br.com.sigvest.api.service;

import br.com.sigvest.api.model.financeiro.ContaCorrente;
import br.com.sigvest.api.model.financeiro.MovimentarFinanceiro;
import br.com.sigvest.api.model.financeiro.StatusFinanceiro;
import br.com.sigvest.api.model.financeiro.TipoFinanceiro;
import br.com.sigvest.api.repository.Financeiro.ContaCorrenteRepository;
import br.com.sigvest.api.repository.Financeiro.MovimentarFinanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovimentarFinanService {

    @Autowired
    private MovimentarFinanRepository movimentarFinanRepository;

    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    @Transactional
    public MovimentarFinanceiro salvar(MovimentarFinanceiro movimentacao) {
        log.info("Salvando movimentação financeira - Tipo: {}, Valor: R$ {}",
                movimentacao.getTipoFinanceiro(), movimentacao.getValor());

        validarMovimentacao(movimentacao);

        MovimentarFinanceiro movimentacaoSalva = movimentarFinanRepository.save(movimentacao);

        log.info("Movimentação financeira salva com ID: {}", movimentacaoSalva.getIdMFinanceiro());

        return movimentacaoSalva;
    }

    @Transactional
    public MovimentarFinanceiro processarPagamento(Long idMovimentacao) {
        MovimentarFinanceiro movimentacao = movimentarFinanRepository.findById(idMovimentacao)
                .orElseThrow(() -> new IllegalArgumentException("Movimentação não encontrada: " + idMovimentacao));

        if (movimentacao.getStatus() == StatusFinanceiro.PAGO) {
            throw new IllegalArgumentException("Movimentação já foi paga");
        }

        movimentacao.setStatus(StatusFinanceiro.PAGO);
        movimentacao.setDataPagamento(new Date());

        // Atualizar saldo da conta corrente se vinculada
        if (movimentacao.getPlanoPagamento() != null &&
                movimentacao.getPlanoPagamento().getContaCorrente() != null) {

            atualizarSaldoConta(movimentacao.getPlanoPagamento().getContaCorrente(),
                    movimentacao.getValor(),
                    movimentacao.getTipoFinanceiro());
        }

        MovimentarFinanceiro movimentacaoPaga = movimentarFinanRepository.save(movimentacao);

        log.info("Pagamento processado - ID: {}, Valor: R$ {}",
                idMovimentacao, movimentacao.getValor());

        return movimentacaoPaga;
    }

    @Transactional
    public MovimentarFinanceiro criarMovimentacaoCompra(BigDecimal valor, String descricao) {
        MovimentarFinanceiro movimentacao = new MovimentarFinanceiro();
        movimentacao.setTipoFinanceiro(TipoFinanceiro.DESPESA);
        movimentacao.setStatus(StatusFinanceiro.PENDENTE);
        movimentacao.setValor(valor);
        movimentacao.setDataLancamento(new Date());
        movimentacao.setDataVencimento(new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000))); // 30 dias
        movimentacao.setNumParcelas(1);

        return salvar(movimentacao);
    }

    @Transactional
    public MovimentarFinanceiro criarMovimentacaoVenda(BigDecimal valor) {
        MovimentarFinanceiro movimentacao = new MovimentarFinanceiro();
        movimentacao.setTipoFinanceiro(TipoFinanceiro.RECEITA);
        movimentacao.setStatus(StatusFinanceiro.PENDENTE);
        movimentacao.setValor(valor);
        movimentacao.setDataLancamento(new Date());
        movimentacao.setDataPagamento(new Date()); // Venda normalmente é à vista
        movimentacao.setDataVencimento(new Date());
        movimentacao.setNumParcelas(1);

        return salvar(movimentacao);
    }

    @Transactional(readOnly = true)
    public List<MovimentarFinanceiro> buscarPorPeriodo(Date inicio, Date fim) {
        return movimentarFinanRepository.findByDataLancamentoBetween(inicio, fim);
    }

    @Transactional(readOnly = true)
    public List<MovimentarFinanceiro> buscarPendentes() {
        return movimentarFinanRepository.findByStatus(StatusFinanceiro.PENDENTE);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularSaldoPeriodo(Date inicio, Date fim) {
        List<MovimentarFinanceiro> movimentacoes = buscarPorPeriodo(inicio, fim);

        BigDecimal receitas = movimentacoes.stream()
                .filter(m -> m.getTipoFinanceiro() == TipoFinanceiro.RECEITA)
                .filter(m -> m.getStatus() == StatusFinanceiro.PAGO)
                .map(MovimentarFinanceiro::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal despesas = movimentacoes.stream()
                .filter(m -> m.getTipoFinanceiro() == TipoFinanceiro.DESPESA)
                .filter(m -> m.getStatus() == StatusFinanceiro.PAGO)
                .map(MovimentarFinanceiro::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return receitas.subtract(despesas);
    }

    private void validarMovimentacao(MovimentarFinanceiro movimentacao) {
        if (movimentacao.getTipoFinanceiro() == null) {
            throw new IllegalArgumentException("Tipo financeiro é obrigatório");
        }

        if (movimentacao.getValor() == null || movimentacao.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        if (movimentacao.getDataLancamento() == null) {
            movimentacao.setDataLancamento(new Date());
        }

        if (movimentacao.getStatus() == null) {
            movimentacao.setStatus(StatusFinanceiro.PENDENTE);
        }
    }

    private void atualizarSaldoConta(ContaCorrente conta, BigDecimal valor, TipoFinanceiro tipo) {
        BigDecimal saldoAtual = conta.getSaldoAtual() != null ? conta.getSaldoAtual() : BigDecimal.ZERO;

        if (tipo == TipoFinanceiro.RECEITA) {
            conta.setSaldoAtual(saldoAtual.add(valor));
        } else if (tipo == TipoFinanceiro.DESPESA) {
            conta.setSaldoAtual(saldoAtual.subtract(valor));
        }

        contaCorrenteRepository.save(conta);

        log.info("Saldo atualizado - Conta: {}, Saldo anterior: R$ {}, Saldo atual: R$ {}",
                conta.getNumeroConta(), saldoAtual, conta.getSaldoAtual());
    }
}