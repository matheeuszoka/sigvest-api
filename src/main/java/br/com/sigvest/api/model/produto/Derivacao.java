package br.com.sigvest.api.model.produto;

import br.com.sigvest.api.model.produto.Roupa.Tamanho;
import br.com.sigvest.api.model.produto.Roupa.tipoCor;
import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
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
@Table(name = "derivacao_sku")
public class Derivacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDerivacao;

    @Column(name = "codigo_sku", nullable = true, length =20 )
    private String codigoSKU;

    @Column(name = "codigo_venda", nullable = true, length =20 )
    private String codigoVenda;

    @Column(name = "preco_custo", nullable = false, length =20 )
    private BigDecimal precoCusto;

    @Column(name = "preco_venda", nullable = false, length =20 )
    private BigDecimal precoVenda;

    @Column(name = "margem_venda", nullable = false, length =10 )
    private BigDecimal margemVenda;

    @Column(name = "estoque_produto", nullable = false, length =10 )
    private Integer estoque;

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
