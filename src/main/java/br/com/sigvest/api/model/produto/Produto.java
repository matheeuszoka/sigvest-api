package br.com.sigvest.api.model.produto;

import br.com.sigvest.api.model.produto.Roupa.Marca;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Derivacao> derivacoes = new ArrayList<>();

    public void adicionarDerivacao(Derivacao d) {
        derivacoes.add(d);
        d.setProduto(this);
    }
}
