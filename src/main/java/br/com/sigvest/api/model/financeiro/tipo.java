package br.com.sigvest.api.model.financeiro;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "tipo_financeiro")
public class tipo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idTipo;

    private String tipo;


}
