package com.vaultgame.api.domain.entity.product.details;

import static com.vaultgame.api.domain.validators.DomainAssert.maxLength;
import static com.vaultgame.api.domain.validators.DomainAssert.min;
import static com.vaultgame.api.domain.validators.DomainAssert.required;
import static com.vaultgame.api.domain.validators.DomainAssert.requiredText;

import com.vaultgame.api.domain.enums.ProductCategory;

public record DigitalCardDetails(
        String redemptionPlatform, String region, Integer validityDays, Boolean instantDelivery)
        implements ProductDetails {

    public DigitalCardDetails {
        validateFields(redemptionPlatform, region, validityDays, instantDelivery);
    }

    @Override
    public ProductCategory category() {
        return ProductCategory.DIGITAL_CARD;
    }

    @Override
    public void validate() {
        validateFields(redemptionPlatform, region, validityDays, instantDelivery);
    }

    private static void validateFields(
            String redemptionPlatform, String region, Integer validityDays, Boolean instantDelivery) {
        requiredText(redemptionPlatform, "Redemption platform is required");
        maxLength(redemptionPlatform, 100, "Redemption platform must have at most 100 characters");
        requiredText(region, "Region is required");
        maxLength(region, 50, "Region must have at most 50 characters");
        required(validityDays, "Validity is required");
        min(validityDays, 1, "Validity must be at least 1 day");
        required(instantDelivery, "Instant delivery status is required");
    }
}
