package com.vaultgame.customer.application.usecase.address;

import com.vaultgame.customer.domain.model.address.AddressType;

public record AddressCommand(
        String label,
        String street,
        String number,
        String complement,
        String district,
        String city,
        String state,
        String postalCode,
        String country,
        AddressType type,
        Boolean isDefault
) {
}
