package br.com.sigvest.api.model.produto;

import br.com.sigvest.api.model.produto.Roupa.Marca;
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
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long idProduto;

    @Column(name = "nome_produto", nullable = false)
    private String nomeProduto;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    // RELACIONAMENTO 1:N - UM PRODUTO TEM VÁRIAS DERIVAÇÕES
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Derivacao> derivacoes = new ArrayList<>();

    // Compatibilidade: aceita "derivacao" singular no JSON
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

    // Método auxiliar para adicionar derivação
    public void adicionarDerivacao(Derivacao d) {
        if (derivacoes == null) {
            derivacoes = new ArrayList<>();
        }
        derivacoes.add(d);
        d.setProduto(this);
    }
}
