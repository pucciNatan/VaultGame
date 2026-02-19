package br.com.vaultgame.vaultgame.dto.jogos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record JogoResponseDTO(
        Long id,
        String titulo,
        String descricao,
        LocalDate dataLancamento,
        BigDecimal preco,
        Set<Long> categoriasIds
) {}
