package br.com.sigvest.api.repository;

import br.com.sigvest.api.model.produto.Derivacao;
import br.com.sigvest.api.model.produto.Produto;
import br.com.sigvest.api.model.produto.Roupa.Tamanho;
import br.com.sigvest.api.model.produto.Roupa.tipoCor;
import br.com.sigvest.api.model.produto.Roupa.tipoRoupa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DerivacaoRepository extends JpaRepository<Derivacao, Long> {

    // Buscar por código SKU único
    Optional<Derivacao> findByCodigoSKU(String codigoSKU);

    // Buscar por código de venda único
    Optional<Derivacao> findByCodigoVenda(String codigoVenda);

    // MÉTODO LEGADO - sem Produto (para compatibilidade)
    @Query("SELECT d FROM Derivacao d WHERE d.tipoRoupa = :tipoRoupa AND d.tipoCor = :tipoCor AND d.tamanho = :tamanho")
    Optional<Derivacao> findByTipoRoupaAndTipoCorAndTamanho(
            @Param("tipoRoupa") tipoRoupa tipoRoupa,
            @Param("tipoCor") tipoCor tipoCor,
            @Param("tamanho") Tamanho tamanho
    );

    // MÉTODO NOVO - com Produto (para modelo 1:N)
    Optional<Derivacao> findByProdutoAndTipoRoupaAndTipoCorAndTamanho(
            Produto produto, tipoRoupa tipoRoupa, tipoCor tipoCor, Tamanho tamanho
    );

    // Buscar derivações de um produto específico
    List<Derivacao> findByProduto(Produto produto);

    // Listar derivações por tipo de roupa
    List<Derivacao> findByTipoRoupa(tipoRoupa tipoRoupa);

    // Buscar por tipo de roupa e cor
    List<Derivacao> findByTipoRoupaAndTipoCor(tipoRoupa tipoRoupa, tipoCor tipoCor);

    // Verificar estoque por derivação
    @Query("SELECT d FROM Derivacao d WHERE d.estoque > 0")
    List<Derivacao> findByEstoqueGreaterThanZero();

    // MÉTODO PARA GERAÇÃO AUTOMÁTICA DE SKU
    @Query(value = "SELECT codigo_sku FROM derivacao_sku " +
            "WHERE codigo_sku LIKE :pattern " +
            "ORDER BY codigo_sku DESC LIMIT 1",
            nativeQuery = true)
    String findLastSKUByPattern(@Param("pattern") String pattern);
}
