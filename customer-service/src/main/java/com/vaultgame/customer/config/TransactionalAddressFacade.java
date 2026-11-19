package com.vaultgame.customer.config;

import com.vaultgame.customer.application.port.in.CreateAddressPort;
import com.vaultgame.customer.application.port.in.UpdateAddressPort;
import com.vaultgame.customer.application.usecase.address.AddressCommand;
import com.vaultgame.customer.application.usecase.address.CreateAddressUseCase;
import com.vaultgame.customer.application.usecase.address.UpdateAddressUseCase;
import com.vaultgame.customer.domain.model.address.Address;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class TransactionalAddressFacade implements CreateAddressPort, UpdateAddressPort {

    private final CreateAddressUseCase createAddressUseCase;
    private final UpdateAddressUseCase updateAddressUseCase;

    public TransactionalAddressFacade(
            CreateAddressUseCase createAddressUseCase,
            UpdateAddressUseCase updateAddressUseCase
    ) {
        this.createAddressUseCase = createAddressUseCase;
        this.updateAddressUseCase = updateAddressUseCase;
    }

    @Override
    @Transactional
    public Address execute(UUID customerId, AddressCommand command) {
        return createAddressUseCase.execute(customerId, command);
    }

    @Override
    @Transactional
    public Address execute(UUID customerId, UUID addressId, AddressCommand command) {
        return updateAddressUseCase.execute(customerId, addressId, command);
    }
}