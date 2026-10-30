package com.vaultgame.customer.adapter.out.persistence;

import com.vaultgame.customer.domain.model.wishlist.WishlistItem;
import com.vaultgame.customer.adapter.out.persistence.repository.SpringDataWishlistRepository;
import com.vaultgame.customer.application.port.out.WishlistRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class WishlistRepositoryAdapter implements WishlistRepositoryPort {

    private final SpringDataWishlistRepository repository;

    public WishlistRepositoryAdapter(SpringDataWishlistRepository repository) {
        this.repository = repository;
    }

    @Override
    public WishlistItem save(WishlistItem item) {
        return PersistenceMapper.toDomain(repository.save(PersistenceMapper.toEntity(item)));
    }

    @Override
    public List<WishlistItem> findByCustomerId(UUID customerId) {
        return repository.findByCustomerIdOrderByAddedAtDesc(customerId).stream()
                .map(PersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<WishlistItem> findByCustomerIdAndProductId(UUID customerId, String productId) {
        return repository.findByCustomerIdAndProductId(customerId, productId).map(PersistenceMapper::toDomain);
    }

    @Override
    public void delete(WishlistItem item) {
        repository.delete(PersistenceMapper.toEntity(item));
    }
}
