package br.com.vaultgame.vaultgame.dto.categorias;

import java.util.Set;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        String descricao,
        Set<Long> jogosIds
) {
}