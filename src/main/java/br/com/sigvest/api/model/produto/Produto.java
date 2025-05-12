package br.com.sigvest.api.model.produto;

import br.com.sigvest.api.model.extras.TipoRoupa;
import jakarta.persistence.*;

import java.math.BigDecimal;

public class Produto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long idProduto;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false,unique = true)
    private String codigo;

    @Column(name = "preco_custo", nullable = false)
    private BigDecimal precoCusto;

    @Column(name = "preco_venda", nullable = false)
    private BigDecimal precoVenda;

    @Column(nullable = false)
    private Integer estoque;

    @Column(nullable = false)
    private String tamanho;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoRoupa tipoRoupa;

    @OneToMany
    @JoinColumn(name = "id_marca")
    private Marca marca;
}
