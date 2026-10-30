package com.vaultgame.customer.application.port.out;

import com.vaultgame.customer.domain.model.address.Address;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepositoryPort {

    Address save(Address address);

    List<Address> findByCustomerId(UUID customerId);

    Optional<Address> findByIdAndCustomerId(UUID id, UUID customerId);

    long countByCustomerId(UUID customerId);

    void delete(Address address);

    void clearDefaultForCustomer(UUID customerId);
}
