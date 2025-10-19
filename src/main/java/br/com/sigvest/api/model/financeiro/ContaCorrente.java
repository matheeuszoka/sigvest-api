package br.com.sigvest.api.model.financeiro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "conta_corrente",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_conta", columnNames = {"banco", "agencia", "numero_conta"})
        }
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ContaCorrente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_c_corrente")
    private Long idCCorrente;

    @Column(name = "banco", length = 100, nullable = false)
    private String banco;

    @Column(name = "agencia", length = 25, nullable = false)
    private String agencia;

    @Column(name = "numero_conta", length = 25, nullable = false)
    private String numeroConta;

    @Column(name = "saldo_inicial", precision = 10, scale = 2, nullable = false)
    private BigDecimal saldoInicial = BigDecimal.ZERO;

    @Column(name = "saldo_atual", precision = 10, scale = 2, nullable = false)
    private BigDecimal saldoAtual = BigDecimal.ZERO;

}
