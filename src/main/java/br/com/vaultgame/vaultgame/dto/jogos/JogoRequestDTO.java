package br.com.vaultgame.vaultgame.dto.jogos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record JogoRequestDTO(
        @NotBlank(message = "titulo não deve estar em branco")
        String titulo,
        String descricao,
        @NotNull(message = "dataLancamento não deve ser nulo")
        LocalDate dataLancamento,
        @NotNull(message = "preco não deve ser nulo")
        @DecimalMin(value = "0", inclusive = false, message = "preco deve ser maior que zero")
        BigDecimal preco,
        Set<Long> categoriasIds
) {}