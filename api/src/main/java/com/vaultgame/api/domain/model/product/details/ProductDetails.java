package com.vaultgame.api.domain.model.product.details;

import com.vaultgame.api.domain.model.enums.ProductCategory;

public sealed interface ProductDetails
        permits GameDetails,
                BoardGameDetails,
                HardwareDetails,
                PeripheralDetails,
                AccessoryDetails,
                FurnitureDetails,
                ActionFigureDetails,
                MerchandiseDetails,
                DigitalCardDetails {

    ProductCategory category();

    void validate();
}
