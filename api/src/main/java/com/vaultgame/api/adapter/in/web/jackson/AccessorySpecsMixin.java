package com.vaultgame.api.adapter.in.web.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vaultgame.api.domain.model.product.details.specs.CableSpecs;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CableSpecs.class, name = "CABLE")
})
interface AccessorySpecsMixin {
}
