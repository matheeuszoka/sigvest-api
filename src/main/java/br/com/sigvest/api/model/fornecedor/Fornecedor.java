package br.com.sigvest.api.model.fornecedor;

import br.com.sigvest.api.model.endereco.Endereco;
import br.com.sigvest.api.model.extras.Tipo;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa um Fornecedor no sistema.
 * Mapeia para a tabela 'fornecedor' no banco de dados.
 */
@Entity
@Table(name = "fornecedor")
@Getter
@Setter
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fornecedor")
    private Long idFornecedor;

    @Column
    private String nomeFantasia;

    @Column
    private String razaoSocial;

    @Column
    private String cpfcnpj;

    @Column
    private String telefone;

    @Column
    private String email;

    @Column
    private Tipo tipo;

    @Embedded
    private Endereco endereco;
}