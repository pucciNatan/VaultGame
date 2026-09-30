package com.vaultgame.customer.infrastructure.persistence;

import com.vaultgame.customer.domain.wishlist.WishlistItem;
import com.vaultgame.customer.domain.wishlist.WishlistRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class WishlistRepositoryAdapter implements WishlistRepository {

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
