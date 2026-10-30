package com.vaultgame.api.domain.model.product.details.specs;

public sealed interface PeripheralSpecs permits MouseSpecs, KeyboardSpecs, HeadsetSpecs {

    void validate();
}
