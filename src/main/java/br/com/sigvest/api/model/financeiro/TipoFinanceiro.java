package br.com.sigvest.api.model.financeiro;

public enum TipoFinanceiro {

    RECEITA("Receita"),
    DESPESA("Despesa"),
    TRANSFERENCIA("Transferência"),
    INVESTIMENTO("Investimento");

    private final String descricao;

    TipoFinanceiro(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}