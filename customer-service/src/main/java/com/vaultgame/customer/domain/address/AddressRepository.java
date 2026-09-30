package com.vaultgame.customer.domain.address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository {

    Address save(Address address);

    List<Address> findByCustomerId(UUID customerId);

    Optional<Address> findByIdAndCustomerId(UUID id, UUID customerId);

    long countByCustomerId(UUID customerId);

    void delete(Address address);

    void clearDefaultForCustomer(UUID customerId);
}
