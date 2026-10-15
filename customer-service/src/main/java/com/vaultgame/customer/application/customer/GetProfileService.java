package com.vaultgame.customer.application.customer;

import com.vaultgame.customer.domain.customer.Customer;
import com.vaultgame.customer.domain.customer.CustomerNotFoundException;
import com.vaultgame.customer.domain.customer.CustomerRepository;

import java.util.UUID;

public class GetProfileService {

    private final CustomerRepository customerRepository;

    public GetProfileService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getProfile(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }
}
