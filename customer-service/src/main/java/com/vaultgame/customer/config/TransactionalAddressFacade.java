package com.vaultgame.customer.config;

import com.vaultgame.customer.application.port.in.CreateAddressPort;
import com.vaultgame.customer.application.port.in.UpdateAddressPort;
import com.vaultgame.customer.application.usecase.address.AddressCommand;
import com.vaultgame.customer.application.usecase.address.AddressUseCase;
import com.vaultgame.customer.domain.model.address.Address;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class TransactionalAddressFacade implements CreateAddressPort, UpdateAddressPort {

    private final AddressUseCase addressUseCase;

    public TransactionalAddressFacade(AddressUseCase addressUseCase) {
        this.addressUseCase = addressUseCase;
    }

    @Override
    @Transactional
    public Address execute(UUID customerId, AddressCommand command) {
        return addressUseCase.execute(customerId, command);
    }

    @Override
    @Transactional
    public Address execute(UUID customerId, UUID addressId, AddressCommand command) {
        return addressUseCase.execute(customerId, addressId, command);
    }
}
