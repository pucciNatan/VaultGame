package com.vaultgame.customer.application.address;

import com.vaultgame.customer.domain.address.AddressType;

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
