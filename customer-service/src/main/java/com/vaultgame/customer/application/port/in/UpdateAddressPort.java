package com.vaultgame.customer.application.port.in;

import com.vaultgame.customer.application.usecase.address.AddressCommand;
import com.vaultgame.customer.domain.model.address.Address;
import java.util.UUID;

public interface UpdateAddressPort {
    Address execute(UUID customerId, UUID addressId, AddressCommand command);
}
