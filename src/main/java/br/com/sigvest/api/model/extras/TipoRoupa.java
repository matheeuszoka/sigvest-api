package br.com.sigvest.api.model.extras;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TipoRoupa {


    TIPO_CAMISETA("tipo_camiseta"),
    TIPO_CALCA("tipo_calca"),
    TIPO_VESTIDO("tipo_vestido"),
    TIPO_MOLETOM("tipo_moletom"),
    TIPO_CASACO("tipo_casaco"),
    TIPO_REGATA("tipo_regata"),
    TIPO_OUTROS("tipo_outros");

    private String tipoRoupa;



}
