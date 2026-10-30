package com.vaultgame.api.adapter.in.web.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vaultgame.api.domain.model.product.details.AccessoryDetails;
import com.vaultgame.api.domain.model.product.details.ActionFigureDetails;
import com.vaultgame.api.domain.model.product.details.BoardGameDetails;
import com.vaultgame.api.domain.model.product.details.DigitalCardDetails;
import com.vaultgame.api.domain.model.product.details.FurnitureDetails;
import com.vaultgame.api.domain.model.product.details.GameDetails;
import com.vaultgame.api.domain.model.product.details.HardwareDetails;
import com.vaultgame.api.domain.model.product.details.MerchandiseDetails;
import com.vaultgame.api.domain.model.product.details.PeripheralDetails;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "kind")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GameDetails.class, name = "GAME"),
        @JsonSubTypes.Type(value = BoardGameDetails.class, name = "BOARD_GAME"),
        @JsonSubTypes.Type(value = HardwareDetails.class, name = "HARDWARE"),
        @JsonSubTypes.Type(value = PeripheralDetails.class, name = "PERIPHERAL"),
        @JsonSubTypes.Type(value = AccessoryDetails.class, name = "ACCESSORY"),
        @JsonSubTypes.Type(value = FurnitureDetails.class, name = "FURNITURE"),
        @JsonSubTypes.Type(value = ActionFigureDetails.class, name = "ACTION_FIGURE"),
        @JsonSubTypes.Type(value = MerchandiseDetails.class, name = "MERCHANDISE"),
        @JsonSubTypes.Type(value = DigitalCardDetails.class, name = "DIGITAL_CARD")
})
interface ProductDetailsMixin {
}
