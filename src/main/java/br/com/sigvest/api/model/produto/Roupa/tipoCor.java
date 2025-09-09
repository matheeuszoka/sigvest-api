package br.com.sigvest.api.model.produto.Roupa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipo_cor")
public class tipoCor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_t_cor")
    private Long IdTCor;

    @Column(name = "nome_cor", length = 20, nullable = false)
    private String nomeCor;


}
