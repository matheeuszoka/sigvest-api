package br.com.sigvest.api.model.produto;

import br.com.sigvest.api.model.compras.ItemCompras;
import br.com.sigvest.api.model.produto.Roupa.Marca;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "produto",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_produto_nome_marca", columnNames = {"nome_produto", "id_marca"})
        }
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long idProduto;

    @Column(name = "nome_produto", nullable = false, length = 150)
    private String nomeProduto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    @OneToMany(mappedBy = "produto", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Derivacao> derivacoes = new ArrayList<>();

    //
    @Transient
    private Derivacao derivacaoCompat;

    @JsonProperty("derivacao")
    public void setDerivacaoCompat(Derivacao d) {
        if (this.derivacoes == null) {
            this.derivacoes = new ArrayList<>();
        }
        if (d != null) {
            this.derivacoes.add(d);
            this.derivacaoCompat = d;
        }
    }

    public void adicionarDerivacao(Derivacao d) {
        if (derivacoes == null) {
            derivacoes = new ArrayList<>();
        }
        derivacoes.add(d);
        d.setProduto(this);
    }
}
