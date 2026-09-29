package com.vaultgame.api.domain.entity.product.details.specs;

public sealed interface PeripheralSpecs permits MouseSpecs, KeyboardSpecs, HeadsetSpecs {

    void validate();
}
