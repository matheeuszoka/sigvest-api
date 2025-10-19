package br.com.sigvest.api.model.produto;

import br.com.sigvest.api.model.compras.ItemCompras;
import br.com.sigvest.api.model.produto.Roupa.Tamanho;
import br.com.sigvest.api.model.produto.Roupa.tipoCor;
import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Derivacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_derivacao")
    private Long idDerivacao;

    @Column(name = "codigo_sku", nullable = false, length = 50)
    private String codigoSKU;

    @Column(name = "codigo_venda", length = 20)
    private String codigoVenda;

    @Column(name = "preco_custo", precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @Column(name = "preco_venda", precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(name = "margem_venda", precision = 5, scale = 2)
    private BigDecimal margemVenda;

    @Column(name = "estoque_produto", nullable = false)
    private Integer estoque = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_produto", nullable = false)
    @JsonBackReference("produto-derivacoes")
    private Produto produto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_t_roupa", nullable = false)
    private tipoRoupa tipoRoupa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tamanho", nullable = false)
    private Tamanho tamanho;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_t_cor", nullable = false)
    private tipoCor tipoCor;

    @OneToMany(mappedBy = "derivacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCompras> itensCompra = new ArrayList<>();


//    @Transient
    @JsonProperty("nomeProduto")
    public String getNomeProduto() {
        return (produto != null) ? produto.getNomeProduto() : null;
    }

//    @Transient
    @JsonProperty("marcaProduto")
    public String getMarcaProduto() {
        return (produto != null && produto.getMarca() != null) ? produto.getMarca().getMarca() : null;
    }


}


