package br.com.sigvest.api.model.financeiro;

public enum StatusFinanceiro {
    PENDENTE("Pendente"),
    PAGO("Pago"),
    VENCIDO("Vencido"),
    CANCELADO("Cancelado"),
    EM_PROCESSAMENTO("Em Processamento");

    private final String descricao;

    StatusFinanceiro(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}