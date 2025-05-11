package br.com.sigvest.api.model.endereco;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um Estado no sistema.
 * Mapeia para a tabela 'estado' no banco de dados.
 */
@Entity
@Table(name = "estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idEstado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long idEstado;

    @Column
    private String nomeEstado;

    @Column
    private String uf;

}

