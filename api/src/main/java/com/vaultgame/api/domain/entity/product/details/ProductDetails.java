package com.vaultgame.api.domain.entity.product.details;

import com.vaultgame.api.domain.enums.ProductCategory;

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
