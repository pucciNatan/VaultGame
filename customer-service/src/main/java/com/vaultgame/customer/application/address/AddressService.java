package com.vaultgame.customer.application.address;

import com.vaultgame.customer.domain.address.Address;
import com.vaultgame.customer.domain.address.AddressLimitExceededException;
import com.vaultgame.customer.domain.address.AddressNotFoundException;
import com.vaultgame.customer.domain.address.AddressRepository;

import java.util.List;
import java.util.UUID;

public class AddressService {

    public static final int MAX_ADDRESSES_PER_CUSTOMER = 20;

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> list(UUID customerId) {
        return addressRepository.findByCustomerId(customerId);
    }

    public Address create(UUID customerId, AddressCommand command) {
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

    public Address update(UUID customerId, UUID addressId, AddressCommand command) {
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

    public void delete(UUID customerId, UUID addressId) {
        Address existing = addressRepository.findByIdAndCustomerId(addressId, customerId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
        addressRepository.delete(existing);
    }
}
