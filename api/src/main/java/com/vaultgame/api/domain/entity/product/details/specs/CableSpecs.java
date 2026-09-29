package com.vaultgame.api.domain.entity.product.details.specs;

import static com.vaultgame.api.domain.DomainAssert.maxLength;
import static com.vaultgame.api.domain.DomainAssert.min;
import static com.vaultgame.api.domain.DomainAssert.required;
import static com.vaultgame.api.domain.DomainAssert.requiredText;

public record CableSpecs(Integer cableLengthCm, String connectorType) implements AccessorySpecs {

    public CableSpecs {
        validateFields(cableLengthCm, connectorType);
    }

    @Override
    public void validate() {
        validateFields(cableLengthCm, connectorType);
    }

    private static void validateFields(Integer cableLengthCm, String connectorType) {
        required(cableLengthCm, "Cable length is required");
        min(cableLengthCm, 1, "Cable length must be at least 1 cm");
        requiredText(connectorType, "Connector type is required");
        maxLength(connectorType, 50, "Connector type must have at most 50 characters");
    }
}
