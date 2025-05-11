package br.com.sigvest.api.model.endereco;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "cidade")
@Getter
@Setter
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cidade")
    private Long idCidade;

    @Column
    private String nomeCidade;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;


}
