package br.com.sigvest.api.model.compras;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estorno_compras")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EstornoCompras implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_e_compras")
    private Long idECompras;

    // Associação à Compra original
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;

    @Column(name = "tipo_estorno", nullable = false, length = 20)
    private String tipoEstorno;

    @Column(name = "motivo_estorno", nullable = false, length = 500)
    private String motivoEstorno;

    @Column(name = "valor_total_estorno", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotalEstorno;

    @Column(name = "data_estorno", nullable = false)
    private LocalDateTime dataEstorno = LocalDateTime.now();


}