package br.com.sigvest.api.model.produto.Roupa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipo_roupa")
public class tipoRoupa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_t_roupa")
    private Long idTRoupa;

    @Column(name="tipo_roupa", nullable = false, length = 20)
    private String tipoRoupa;



}
