package br.com.sigvest.api.model.pessoa;

import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.extras.Atribuicao;
import br.com.sigvest.api.model.extras.Tipo;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "name_completo", nullable = false)
    private String nomeCompleto;

    @Column(name = "data_nascimento")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @Column(name = "cpf_cnpj", nullable = false, unique = true)
    private String cpfcnpj;

    @Column(nullable = false, unique = true)
    private String rg;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Atribuicao atrib;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "idEndereco", referencedColumnName = "idEndereco")
    private Endereco endereco;

}