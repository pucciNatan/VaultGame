package com.vaultgame.customer.adapter.out.persistence;

import com.vaultgame.customer.domain.model.customer.Customer;
import com.vaultgame.customer.adapter.out.persistence.repository.SpringDataCustomerRepository;
import com.vaultgame.customer.application.port.out.CustomerRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final SpringDataCustomerRepository repository;

    public CustomerRepositoryAdapter(SpringDataCustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer save(Customer customer) {
        return PersistenceMapper.toDomain(repository.save(PersistenceMapper.toEntity(customer)));
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return repository.findById(id).map(PersistenceMapper::toDomain);
    }
}
