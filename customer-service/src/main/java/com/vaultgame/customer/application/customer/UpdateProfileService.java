package com.vaultgame.customer.application.customer;

import com.vaultgame.customer.domain.customer.Customer;
import com.vaultgame.customer.domain.customer.CustomerNotFoundException;
import com.vaultgame.customer.domain.customer.CustomerRepository;

import java.util.UUID;

public class UpdateProfileService {

    private final CustomerRepository customerRepository;

    public UpdateProfileService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer updateProfile(UUID customerId, UpdateProfileCommand command) {
        Customer existing = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        Customer updated = existing.withProfileUpdate(command.fullName(), command.phone());
        return customerRepository.save(updated);
    }
}
