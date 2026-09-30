package com.vaultgame.customer.infrastructure.persistence;

import com.vaultgame.customer.domain.customer.Customer;
import com.vaultgame.customer.domain.customer.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryAdapter implements CustomerRepository {

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
