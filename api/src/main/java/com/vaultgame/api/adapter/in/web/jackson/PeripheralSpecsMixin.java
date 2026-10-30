package com.vaultgame.api.adapter.in.web.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vaultgame.api.domain.model.product.details.specs.HeadsetSpecs;
import com.vaultgame.api.domain.model.product.details.specs.KeyboardSpecs;
import com.vaultgame.api.domain.model.product.details.specs.MouseSpecs;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MouseSpecs.class, name = "MOUSE"),
        @JsonSubTypes.Type(value = KeyboardSpecs.class, name = "KEYBOARD"),
        @JsonSubTypes.Type(value = HeadsetSpecs.class, name = "HEADSET")
})
interface PeripheralSpecsMixin {
}
