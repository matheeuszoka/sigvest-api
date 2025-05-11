package br.com.sigvest.api.model.extras;

public enum Tipo {

    PESSOA_FISICA("pessoa_fisica"),
    PESSOA_JURIDICA("pessoa_juridica");

    private String tipo;

    Tipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
