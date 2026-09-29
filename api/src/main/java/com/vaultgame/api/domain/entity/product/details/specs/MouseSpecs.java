package com.vaultgame.api.domain.entity.product.details.specs;

import static com.vaultgame.api.domain.DomainAssert.min;
import static com.vaultgame.api.domain.DomainAssert.required;

public record MouseSpecs(Integer dpi) implements PeripheralSpecs {

    public MouseSpecs {
        validateFields(dpi);
    }

    @Override
    public void validate() {
        validateFields(dpi);
    }

    private static void validateFields(Integer dpi) {
        required(dpi, "DPI is required");
        min(dpi, 1, "DPI must be at least 1");
    }
}
