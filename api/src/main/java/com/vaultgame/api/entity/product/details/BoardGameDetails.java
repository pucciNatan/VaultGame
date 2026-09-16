package com.vaultgame.api.entity.product.details;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("BOARD_GAME")
public final class BoardGameDetails implements ProductDetails {

    @NotNull(message = "Minimum players is required")
    @Min(value = 1, message = "Minimum players must be at least 1")
    private Integer minPlayers;

    @NotNull(message = "Maximum players is required")
    @Min(value = 1, message = "Maximum players must be at least 1")
    private Integer maxPlayers;

    @NotNull(message = "Play time is required")
    @Min(value = 1, message = "Play time must be at least 1 minute")
    private Integer playTimeMinutes;

    @NotNull(message = "Recommended age is required")
    @Min(value = 0, message = "Recommended age cannot be negative")
    private Integer recommendedAge;

    @AssertTrue(message = "Maximum players must be greater than or equal to minimum players")
    public boolean isPlayerRangeValid() {
        if (minPlayers == null || maxPlayers == null) {
            return true;
        }
        return maxPlayers >= minPlayers;
    }
}
