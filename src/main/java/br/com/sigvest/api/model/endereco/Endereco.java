package br.com.sigvest.api.model.endereco;

import br.com.sigvest.api.model.fornecedor.Fornecedor;
import br.com.sigvest.api.model.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.*;

//@Entity
//@Table(name = "endereco")
@Embeddable
@Getter
@Setter
public class Endereco {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_endereco")
//    private Long idEndereco;

    @Column
    private String logradouro;

    @Column(name = "numero")
    private String numero;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cep")
    private String cep;

    @ManyToOne
    @JoinColumn(name = "id_cidade", nullable = false)
    private Cidade cidade;


}