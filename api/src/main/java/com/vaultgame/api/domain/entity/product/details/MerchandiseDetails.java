package com.vaultgame.api.domain.entity.product.details;

import static com.vaultgame.api.domain.DomainAssert.maxLength;
import static com.vaultgame.api.domain.DomainAssert.requiredText;

import com.vaultgame.api.domain.enums.ProductCategory;

public record MerchandiseDetails(
        String size, String color, String material, String license, String careInstructions)
        implements ProductDetails {

    public MerchandiseDetails {
        validateFields(size, color, material, license, careInstructions);
    }

    @Override
    public ProductCategory category() {
        return ProductCategory.MERCHANDISE;
    }

    @Override
    public void validate() {
        validateFields(size, color, material, license, careInstructions);
    }

    private static void validateFields(
            String size, String color, String material, String license, String careInstructions) {
        maxLength(size, 20, "Size must have at most 20 characters");
        requiredText(color, "Color is required");
        maxLength(color, 50, "Color must have at most 50 characters");
        requiredText(material, "Material is required");
        maxLength(material, 100, "Material must have at most 100 characters");
        requiredText(license, "License is required");
        maxLength(license, 150, "License must have at most 150 characters");
        requiredText(careInstructions, "Care instructions are required");
        maxLength(careInstructions, 500, "Care instructions must have at most 500 characters");
    }
}
