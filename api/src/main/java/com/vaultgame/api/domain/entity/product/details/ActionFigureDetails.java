package com.vaultgame.api.domain.entity.product.details;

import static com.vaultgame.api.domain.DomainAssert.maxLength;
import static com.vaultgame.api.domain.DomainAssert.positive;
import static com.vaultgame.api.domain.DomainAssert.required;
import static com.vaultgame.api.domain.DomainAssert.requiredText;

import com.vaultgame.api.domain.enums.ProductCategory;
import java.math.BigDecimal;

public record ActionFigureDetails(
        String franchise,
        String character,
        BigDecimal heightCm,
        String material,
        Boolean articulated,
        String scale)
        implements ProductDetails {

    public ActionFigureDetails {
        validateFields(franchise, character, heightCm, material, articulated, scale);
    }

    @Override
    public ProductCategory category() {
        return ProductCategory.ACTION_FIGURE;
    }

    @Override
    public void validate() {
        validateFields(franchise, character, heightCm, material, articulated, scale);
    }

    private static void validateFields(
            String franchise,
            String character,
            BigDecimal heightCm,
            String material,
            Boolean articulated,
            String scale) {
        requiredText(franchise, "Franchise is required");
        maxLength(franchise, 150, "Franchise must have at most 150 characters");
        requiredText(character, "Character is required");
        maxLength(character, 150, "Character must have at most 150 characters");
        required(heightCm, "Height is required");
        positive(heightCm, "Height must be greater than zero");
        requiredText(material, "Material is required");
        maxLength(material, 100, "Material must have at most 100 characters");
        required(articulated, "Articulated status is required");
        requiredText(scale, "Scale is required");
        maxLength(scale, 20, "Scale must have at most 20 characters");
    }
}
