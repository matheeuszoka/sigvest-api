package br.com.sigvest.api.model.extras;

public enum Atribuicao {

    FUNCIONARIO("funcionario"),
    CLIENTE("cliente"),
    FORNECEDOR("fornecedor");
    private String atribuicao;

    Atribuicao(String atribuicao) {
        this.atribuicao = atribuicao;
    }

    public String getAtribuicao() {
        return atribuicao;
    }
}
