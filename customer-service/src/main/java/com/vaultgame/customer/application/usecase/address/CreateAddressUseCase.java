package com.vaultgame.customer.application.usecase.address;

import com.vaultgame.customer.application.port.in.CreateAddressPort;
import com.vaultgame.customer.application.port.out.AddressRepositoryPort;
import com.vaultgame.customer.domain.exception.AddressLimitExceededException;
import com.vaultgame.customer.domain.model.address.Address;
import java.util.UUID;

public class CreateAddressUseCase implements CreateAddressPort {

    public static final int MAX_ADDRESSES_PER_CUSTOMER = 20;

    private final AddressRepositoryPort addressRepository;

    public CreateAddressUseCase(AddressRepositoryPort addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address execute(UUID customerId, AddressCommand command) {
        if (addressRepository.countByCustomerId(customerId) >= MAX_ADDRESSES_PER_CUSTOMER) {
            throw new AddressLimitExceededException(MAX_ADDRESSES_PER_CUSTOMER);
        }
        boolean isDefault = command.isDefault() != null && command.isDefault();
        if (isDefault) {
            addressRepository.clearDefaultForCustomer(customerId);
        }
        Address address = Address.create(
                customerId,
                command.label(),
                command.street(),
                command.number(),
                command.complement(),
                command.district(),
                command.city(),
                command.state(),
                command.postalCode(),
                command.country(),
                command.type(),
                isDefault
        );
        return addressRepository.save(address);
    }
}