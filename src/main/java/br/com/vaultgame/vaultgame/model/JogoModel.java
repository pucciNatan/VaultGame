package br.com.vaultgame.vaultgame.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"categorias"})
@Entity
@Table(name = "jogos")
public class JogoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, name = "data_lancamento")
    private LocalDate dataLancamento;

    @Column(nullable = false ,precision = 10, scale = 2)
    private BigDecimal preco;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "jogos_categorias",
            joinColumns = @JoinColumn(name = "jogo_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<CategoriaModel> categorias = new HashSet<>();
}
