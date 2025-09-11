package br.com.sigvest.api.model.produto;

import br.com.sigvest.api.model.produto.Roupa.Tamanho;
import br.com.sigvest.api.model.produto.Roupa.tipoCor;
import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "derivacao_sku",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_derivacao_sku", columnNames = {"codigo_sku"}),
                @UniqueConstraint(name = "uk_derivacao_comb", columnNames = {"id_produto", "id_t_roupa", "id_t_cor", "id_tamanho"})
        }
)
public class Derivacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_derivacao")
    private Long idDerivacao;

    @Column(name = "codigo_sku", nullable = false, length = 50)
    private String codigoSKU;

    @Column(name = "codigo_venda", length = 20)
    private String codigoVenda;

    @Column(name = "preco_custo", nullable = false)
    private BigDecimal precoCusto;

    @Column(name = "preco_venda", nullable = false)
    private BigDecimal precoVenda;

    @Column(name = "margem_venda", nullable = false)
    private BigDecimal margemVenda;

    @Column(name = "estoque_produto", nullable = false)
    private Integer estoque;

    // RELACIONAMENTO N:1 - DERIVACAO PERTENCE A UM PRODUTO
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_produto", nullable = false)
    @JsonBackReference
    private Produto produto;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_t_roupa", nullable = false)
    private tipoRoupa tipoRoupa;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tamanho", nullable = false)
    private Tamanho tamanho;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_t_cor", nullable = false)
    private tipoCor tipoCor;
}
