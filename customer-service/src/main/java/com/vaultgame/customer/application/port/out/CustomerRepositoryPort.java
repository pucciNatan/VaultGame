package com.vaultgame.customer.application.port.out;

import com.vaultgame.customer.domain.model.customer.Customer;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {

    Customer save(Customer customer);

    Optional<Customer> findById(UUID id);
}
