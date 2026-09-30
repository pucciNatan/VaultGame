package com.vaultgame.api.domain.entity.product.details.specs;

import static com.vaultgame.api.domain.validators.DomainAssert.maxLength;
import static com.vaultgame.api.domain.validators.DomainAssert.requiredText;

public record KeyboardSpecs(String switchType) implements PeripheralSpecs {

    public KeyboardSpecs {
        validateFields(switchType);
    }

    @Override
    public void validate() {
        validateFields(switchType);
    }

    private static void validateFields(String switchType) {
        requiredText(switchType, "Switch type is required");
        maxLength(switchType, 100, "Switch type must have at most 100 characters");
    }
}
