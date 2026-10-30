package com.vaultgame.api.domain.model.product.details.specs;

public sealed interface AccessorySpecs permits CableSpecs {

    void validate();
}
