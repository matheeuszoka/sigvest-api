package br.com.sigvest.api.model.extras;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TipoRoupa {


    TIPO_CAMISETA("tipo_camiseta"),
    TIPO_CALCA("tipo_calca"),
    TIPO_VESTIDO("tipo_vestido"),
    TIPO_MOLETOM("tipo_moletom"),
    TIPO_JAQUETA("tipo_jaqueta");

    private String tipoRoupa;



}
