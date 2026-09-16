package com.vaultgame.api.entity.product.details;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GameDetails.class, name = "GAME"),
        @JsonSubTypes.Type(value = BoardGameDetails.class, name = "BOARD_GAME"),
        @JsonSubTypes.Type(value = AccessoryDetails.class, name = "ACCESSORY"),
        @JsonSubTypes.Type(value = ActionFigureDetails.class, name = "ACTION_FIGURE"),
        @JsonSubTypes.Type(value = GamingChairDetails.class, name = "GAMING_CHAIR")
})
public sealed interface ProductDetails
        permits GameDetails, BoardGameDetails, AccessoryDetails, ActionFigureDetails, GamingChairDetails {
}
