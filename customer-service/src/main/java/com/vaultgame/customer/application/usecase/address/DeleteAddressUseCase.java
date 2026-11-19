package com.vaultgame.customer.application.usecase.address;

import com.vaultgame.customer.application.port.in.DeleteAddressPort;
import com.vaultgame.customer.application.port.out.AddressRepositoryPort;
import com.vaultgame.customer.domain.exception.AddressNotFoundException;
import com.vaultgame.customer.domain.model.address.Address;
import java.util.UUID;

public class DeleteAddressUseCase implements DeleteAddressPort {

    private final AddressRepositoryPort addressRepository;

    public DeleteAddressUseCase(AddressRepositoryPort addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public void execute(UUID customerId, UUID addressId) {
        Address existing = addressRepository.findByIdAndCustomerId(addressId, customerId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
        addressRepository.delete(existing);
    }
}