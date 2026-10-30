package com.vaultgame.api.domain.model.product.details.specs;

import static com.vaultgame.api.domain.service.DomainAssert.min;

public record HeadsetSpecs(Integer batteryLifeHours) implements PeripheralSpecs {

    public HeadsetSpecs {
        validateFields(batteryLifeHours);
    }

    @Override
    public void validate() {
        validateFields(batteryLifeHours);
    }

    private static void validateFields(Integer batteryLifeHours) {
        min(batteryLifeHours, 1, "Battery life must be at least 1 hour");
    }
}
