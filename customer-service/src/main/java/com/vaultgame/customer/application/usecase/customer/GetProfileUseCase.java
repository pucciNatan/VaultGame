package com.vaultgame.customer.application.usecase.customer;

import com.vaultgame.customer.application.port.in.GetProfilePort;
import com.vaultgame.customer.application.port.out.CustomerRepositoryPort;
import com.vaultgame.customer.domain.exception.CustomerNotFoundException;
import com.vaultgame.customer.domain.model.customer.Customer;
import java.util.UUID;

public class GetProfileUseCase implements GetProfilePort {

    private final CustomerRepositoryPort customerRepository;

    public GetProfileUseCase(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer execute(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }
}
