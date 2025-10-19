package br.com.sigvest.api.model.financeiro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plano_pagamento")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class PlanoPagamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Long idTipo;

    @Column(name = "tipo_financeiro", nullable = false, length = 100)
    private String tipo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_c_corrente", referencedColumnName = "id_c_corrente", nullable = false)
    private ContaCorrente contaCorrente;
}
