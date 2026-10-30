package com.vaultgame.customer.adapter.out.persistence;

import com.vaultgame.customer.domain.model.address.Address;
import com.vaultgame.customer.adapter.out.persistence.repository.SpringDataAddressRepository;
import com.vaultgame.customer.application.port.out.AddressRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AddressRepositoryAdapter implements AddressRepositoryPort {

    private final SpringDataAddressRepository repository;

    public AddressRepositoryAdapter(SpringDataAddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public Address save(Address address) {
        return PersistenceMapper.toDomain(repository.save(PersistenceMapper.toEntity(address)));
    }

    @Override
    public List<Address> findByCustomerId(UUID customerId) {
        return repository.findByCustomerIdOrderByCreatedAtAsc(customerId).stream()
                .map(PersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Address> findByIdAndCustomerId(UUID id, UUID customerId) {
        return repository.findByIdAndCustomerId(id, customerId).map(PersistenceMapper::toDomain);
    }

    @Override
    public long countByCustomerId(UUID customerId) {
        return repository.countByCustomerId(customerId);
    }

    @Override
    public void delete(Address address) {
        repository.deleteById(address.id());
    }

    @Override
    @Transactional
    public void clearDefaultForCustomer(UUID customerId) {
        repository.clearDefaultForCustomer(customerId);
    }
}
