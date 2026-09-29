package com.vaultgame.api.domain.entity.product.details.specs;

public sealed interface AccessorySpecs permits CableSpecs {

    void validate();
}
