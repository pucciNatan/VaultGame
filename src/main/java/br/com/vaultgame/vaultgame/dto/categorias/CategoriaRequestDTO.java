package br.com.vaultgame.vaultgame.dto.categorias;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CategoriaRequestDTO(
        @NotBlank(message = "nome não deve estar em branco")
        String nome,
        String descricao,
        Set<Long> jogosIds
) {}
