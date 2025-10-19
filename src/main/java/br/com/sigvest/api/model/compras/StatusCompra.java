package br.com.sigvest.api.model.compras;

public enum StatusCompra {
    ATIVA,        // Compra normal, sem estorno
    ESTORNADA_PARCIAL, // Teve um ou mais estornos parciais
    ESTORNADA_TOTAL,  // Cancelada completamente
    CANCELADA_FINANCEIRO // (Opcional) Cancelada por outro motivo que não estorno de item
}
