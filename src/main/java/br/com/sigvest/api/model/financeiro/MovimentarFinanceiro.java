package br.com.sigvest.api.model.financeiro;

import br.com.sigvest.api.model.compras.Compra;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movimentar_financeiro")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MovimentarFinanceiro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_m_financeiro")
    private Long idMFinanceiro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_financeiro", nullable = false, length = 20)
    private TipoFinanceiro tipoFinanceiro;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusFinanceiro status = StatusFinanceiro.PENDENTE;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_lancamento", nullable = false)
    private Date dataLancamento;

    @Column(name = "data_pagamento")
    private Date dataPagamento;

    @Column(name = "data_vencimento", nullable = false)
    private Date dataVencimento;

    @Column(name = "numero_parcelas", nullable = false)
    private Integer numParcelas = 1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo")
    private PlanoPagamento planoPagamento;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @OneToOne(mappedBy = "movimentarFinanceiro", fetch = FetchType.LAZY)
    @JsonBackReference("compra-financeiro")
    private Compra compra;
}
