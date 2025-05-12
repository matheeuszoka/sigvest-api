package br.com.sigvest.api.model.fornecedor;

import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.extras.Tipo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fornecedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fornecedor")
    private Long idFornecedor;

    @Column(nullable = false, name = "nome_fantasia")
    private String nomeFantasia;

    @Column(nullable = false, name = "razao_social")
    private String razaoSocial;

    @Column(unique = true, nullable = false, name = "cpf_cnpj")
    private String cpfcnpj;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Embedded
    private Endereco endereco;
}