package com.vaultgame.customer.domain.address;

import java.util.UUID;

public class AddressNotFoundException extends RuntimeException {

    public AddressNotFoundException(UUID addressId) {
        super("Address not found: " + addressId);
    }
}
