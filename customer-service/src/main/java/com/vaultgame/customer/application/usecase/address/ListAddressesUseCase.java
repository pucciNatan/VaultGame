package com.vaultgame.customer.application.usecase.address;

import com.vaultgame.customer.application.port.in.ListAddressesPort;
import com.vaultgame.customer.application.port.out.AddressRepositoryPort;
import com.vaultgame.customer.domain.model.address.Address;
import java.util.List;
import java.util.UUID;

public class ListAddressesUseCase implements ListAddressesPort {

    private final AddressRepositoryPort addressRepository;

    public ListAddressesUseCase(AddressRepositoryPort addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> execute(UUID customerId) {
        return addressRepository.findByCustomerId(customerId);
    }
}