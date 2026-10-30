package com.vaultgame.customer.application.usecase.customer;

import com.vaultgame.customer.application.port.in.UpdateProfilePort;
import com.vaultgame.customer.application.port.out.CustomerRepositoryPort;
import com.vaultgame.customer.domain.exception.CustomerNotFoundException;
import com.vaultgame.customer.domain.model.customer.Customer;
import java.util.UUID;

public class UpdateProfileUseCase implements UpdateProfilePort {

    private final CustomerRepositoryPort customerRepository;

    public UpdateProfileUseCase(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer execute(UUID customerId, UpdateProfileCommand command) {
        Customer existing = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        Customer updated = existing.withProfileUpdate(command.fullName(), command.phone());
        return customerRepository.save(updated);
    }
}
