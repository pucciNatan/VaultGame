package com.vaultgame.customer.application.port.in;

import java.util.UUID;

public interface DeleteAddressPort {
    void execute(UUID customerId, UUID addressId);
}
