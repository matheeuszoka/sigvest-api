package br.com.sigvest.api.model.compras;

import br.com.sigvest.api.model.financeiro.MovimentarFinanceiro;
import br.com.sigvest.api.model.fornecedor.Fornecedor;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compra")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Compra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Long idCompra;

    @Column(name = "numero_nota", nullable = false, length = 100)
    private String numeroNota;

    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    @Column(name = "frete_incluso")
    private Boolean freteIncluso = false;

    @Column(name = "valor_frete", precision = 10, scale = 2)
    private BigDecimal valorFrete = BigDecimal.ZERO;

    @Column(name = "total_compra", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id_fornecedor", nullable = false)
    private Fornecedor fornecedor;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("compra-itens")
    @JsonAlias({"itemCompras"})
    private List<ItemCompras> itens = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_m_financeiro", referencedColumnName = "id_m_financeiro")
    @JsonManagedReference("compra-financeiro")
    private MovimentarFinanceiro movimentarFinanceiro;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_compra", nullable = false, length = 30)
    private StatusCompra status = StatusCompra.ATIVA;
}
