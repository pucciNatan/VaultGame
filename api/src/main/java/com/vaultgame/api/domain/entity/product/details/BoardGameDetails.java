package com.vaultgame.api.domain.entity.product.details;

import static com.vaultgame.api.domain.DomainAssert.maxLength;
import static com.vaultgame.api.domain.DomainAssert.min;
import static com.vaultgame.api.domain.DomainAssert.required;
import static com.vaultgame.api.domain.DomainAssert.requiredText;

import com.vaultgame.api.domain.enums.ProductCategory;
import com.vaultgame.api.domain.exception.DomainException;

public record BoardGameDetails(
        Integer minPlayers,
        Integer maxPlayers,
        Integer playTimeMinutes,
        Integer minAge,
        String language,
        Boolean includesExpansions)
        implements ProductDetails {

    public BoardGameDetails {
        validateFields(minPlayers, maxPlayers, playTimeMinutes, minAge, language, includesExpansions);
    }

    @Override
    public ProductCategory category() {
        return ProductCategory.BOARD_GAME;
    }

    @Override
    public void validate() {
        validateFields(minPlayers, maxPlayers, playTimeMinutes, minAge, language, includesExpansions);
    }

    private static void validateFields(
            Integer minPlayers,
            Integer maxPlayers,
            Integer playTimeMinutes,
            Integer minAge,
            String language,
            Boolean includesExpansions) {
        required(minPlayers, "Minimum players is required");
        min(minPlayers, 1, "Minimum players must be at least 1");
        required(maxPlayers, "Maximum players is required");
        min(maxPlayers, 1, "Maximum players must be at least 1");
        required(playTimeMinutes, "Play time is required");
        min(playTimeMinutes, 1, "Play time must be at least 1 minute");
        required(minAge, "Minimum age is required");
        min(minAge, 0, "Minimum age cannot be negative");
        requiredText(language, "Language is required");
        maxLength(language, 50, "Language must have at most 50 characters");
        required(includesExpansions, "Includes expansions status is required");
        if (maxPlayers < minPlayers) {
            throw new DomainException("Maximum players must be greater than or equal to minimum players");
        }
    }
}
