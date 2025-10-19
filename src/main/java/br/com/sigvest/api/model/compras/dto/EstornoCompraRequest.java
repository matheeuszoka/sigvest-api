package br.com.sigvest.api.model.compras.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class EstornoCompraRequest {

    private Long idCompra;
    private String tipoEstorno; // 'total' ou 'parcial'
    private String motivoEstorno;
    private BigDecimal valorTotalEstorno; // Valor já calculado no frontend
    private List<ItemEstornoDTO> itensEstorno;

    @Data
    public static class ItemEstornoDTO {
        private Long idItemCompra; // ID do ItemCompras a ser estornado
        private BigDecimal quantidadeEstorno;
    }
}
