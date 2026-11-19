package com.vaultgame.customer.application.usecase.address;

import com.vaultgame.customer.application.port.in.UpdateAddressPort;
import com.vaultgame.customer.application.port.out.AddressRepositoryPort;
import com.vaultgame.customer.domain.exception.AddressNotFoundException;
import com.vaultgame.customer.domain.model.address.Address;
import java.util.UUID;

public class UpdateAddressUseCase implements UpdateAddressPort {

    private final AddressRepositoryPort addressRepository;

    public UpdateAddressUseCase(AddressRepositoryPort addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address execute(UUID customerId, UUID addressId, AddressCommand command) {
        Address existing = addressRepository.findByIdAndCustomerId(addressId, customerId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
        boolean isDefault = command.isDefault() != null ? command.isDefault() : existing.isDefault();
        if (isDefault) {
            addressRepository.clearDefaultForCustomer(customerId);
        }
        Address updated = existing.update(
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
        return addressRepository.save(updated);
    }
}