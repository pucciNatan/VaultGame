package com.vaultgame.api.domain.model.product.details.specs;

import static com.vaultgame.api.domain.service.DomainAssert.maxLength;
import static com.vaultgame.api.domain.service.DomainAssert.requiredText;

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
