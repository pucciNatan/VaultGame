package com.vaultgame.api.domain.model.product.details.specs;

import static com.vaultgame.api.domain.service.DomainAssert.maxLength;
import static com.vaultgame.api.domain.service.DomainAssert.min;
import static com.vaultgame.api.domain.service.DomainAssert.required;
import static com.vaultgame.api.domain.service.DomainAssert.requiredText;

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
