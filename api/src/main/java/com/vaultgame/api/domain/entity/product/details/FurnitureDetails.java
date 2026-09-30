package com.vaultgame.api.domain.entity.product.details;

import static com.vaultgame.api.domain.validators.DomainAssert.maxLength;
import static com.vaultgame.api.domain.validators.DomainAssert.min;
import static com.vaultgame.api.domain.validators.DomainAssert.positive;
import static com.vaultgame.api.domain.validators.DomainAssert.required;
import static com.vaultgame.api.domain.validators.DomainAssert.requiredText;

import com.vaultgame.api.domain.enums.ProductCategory;
import java.math.BigDecimal;

public record FurnitureDetails(
        Integer maxWeightKg,
        String material,
        BigDecimal widthCm,
        BigDecimal heightCm,
        BigDecimal depthCm,
        Boolean ergonomicAdjustments,
        Boolean assemblyRequired)
        implements ProductDetails {

    public FurnitureDetails {
        validateFields(
                maxWeightKg, material, widthCm, heightCm, depthCm, ergonomicAdjustments, assemblyRequired);
    }

    @Override
    public ProductCategory category() {
        return ProductCategory.FURNITURE;
    }

    @Override
    public void validate() {
        validateFields(
                maxWeightKg, material, widthCm, heightCm, depthCm, ergonomicAdjustments, assemblyRequired);
    }

    private static void validateFields(
            Integer maxWeightKg,
            String material,
            BigDecimal widthCm,
            BigDecimal heightCm,
            BigDecimal depthCm,
            Boolean ergonomicAdjustments,
            Boolean assemblyRequired) {
        required(maxWeightKg, "Maximum weight is required");
        min(maxWeightKg, 1, "Maximum weight must be at least 1 kg");
        requiredText(material, "Material is required");
        maxLength(material, 100, "Material must have at most 100 characters");
        required(widthCm, "Width is required");
        positive(widthCm, "Width must be greater than zero");
        required(heightCm, "Height is required");
        positive(heightCm, "Height must be greater than zero");
        required(depthCm, "Depth is required");
        positive(depthCm, "Depth must be greater than zero");
        required(ergonomicAdjustments, "Ergonomic adjustments status is required");
        required(assemblyRequired, "Assembly required status is required");
    }
}
