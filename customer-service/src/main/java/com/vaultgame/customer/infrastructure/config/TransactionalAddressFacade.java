package com.vaultgame.customer.infrastructure.config;

import com.vaultgame.customer.application.address.AddressCommand;
import com.vaultgame.customer.application.address.AddressService;
import com.vaultgame.customer.domain.address.Address;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class TransactionalAddressFacade {

    private final AddressService addressService;

    public TransactionalAddressFacade(AddressService addressService) {
        this.addressService = addressService;
    }

    @Transactional
    public Address create(UUID customerId, AddressCommand command) {
        return addressService.create(customerId, command);
    }

    @Transactional
    public Address update(UUID customerId, UUID addressId, AddressCommand command) {
        return addressService.update(customerId, addressId, command);
    }
}
