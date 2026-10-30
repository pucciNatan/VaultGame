package com.vaultgame.customer.application.usecase.address;

import com.vaultgame.customer.application.port.in.CreateAddressPort;
import com.vaultgame.customer.application.port.in.DeleteAddressPort;
import com.vaultgame.customer.application.port.in.ListAddressesPort;
import com.vaultgame.customer.application.port.in.UpdateAddressPort;
import com.vaultgame.customer.application.port.out.AddressRepositoryPort;
import com.vaultgame.customer.domain.exception.AddressLimitExceededException;
import com.vaultgame.customer.domain.exception.AddressNotFoundException;
import com.vaultgame.customer.domain.model.address.Address;
import java.util.List;
import java.util.UUID;

public class AddressUseCase implements ListAddressesPort, CreateAddressPort, UpdateAddressPort, DeleteAddressPort {

    public static final int MAX_ADDRESSES_PER_CUSTOMER = 20;

    private final AddressRepositoryPort addressRepository;

    public AddressUseCase(AddressRepositoryPort addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> execute(UUID customerId) {
        return addressRepository.findByCustomerId(customerId);
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

    @Override
    public void execute(UUID customerId, UUID addressId) {
        Address existing = addressRepository.findByIdAndCustomerId(addressId, customerId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
        addressRepository.delete(existing);
    }
}
