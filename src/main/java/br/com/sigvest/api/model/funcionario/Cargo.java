package br.com.sigvest.api.model.funcionario;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "cargos")
@Data
public class Cargo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Long idCargo;

    @Column(name = "nome_cargo", nullable = false, length = 50)
    private String nomeCargo;

    @Column(name = "salario_bruto", nullable = false, length =20 )
    private BigDecimal salarioBruto;

    @Column(nullable = false, length = 10)
    private Double desconto;

    @Column(name = "salario_liquido", nullable = false, length = 20)
    private BigDecimal salarioLiquido;

    @Column(nullable = false)
    private Boolean status =true;


}
