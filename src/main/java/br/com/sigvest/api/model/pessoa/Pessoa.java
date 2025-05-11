package br.com.sigvest.api.model.pessoa;

import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.extras.Atribuicao;
import br.com.sigvest.api.model.extras.Tipo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "pessoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa")
    private Long idPessoa;

    @Column
    private String nomeCompleto;

    @Column(name = "data_nascimento")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @Column
    private String cpfcnpj;

    @Column
    private String telefone;

    @Column
    private String email;

    @Column
    private Tipo tipo;

    @Column
    private Atribuicao atrib;

    @Embedded
    private Endereco endereco;


//    public boolean getEndereco() {
//    }
}