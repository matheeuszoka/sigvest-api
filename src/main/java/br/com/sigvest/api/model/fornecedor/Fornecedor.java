package br.com.sigvest.api.model.fornecedor;

import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.extras.Tipo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fornecedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fornecedor")
    private Long idFornecedor;

    @Column(nullable = false, name = "nome_fantasia")
    private String nomeFantasia;

    @Column(nullable = false, name = "razao_social")
    private String razaoSocial;

    @Column(nullable = false, name = "inscricao_estadual")
    private String inscricaoEstadual;

    @Column(unique = true, nullable = false, name = "cnpj")
    private String cnpjFornecedor;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "idEndereco", referencedColumnName = "idEndereco")
    private Endereco endereco;


}