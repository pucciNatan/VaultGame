package com.vaultgame.customer.application.port.in;

import com.vaultgame.customer.domain.model.address.Address;
import java.util.List;
import java.util.UUID;

public interface ListAddressesPort {
    List<Address> execute(UUID customerId);
}
