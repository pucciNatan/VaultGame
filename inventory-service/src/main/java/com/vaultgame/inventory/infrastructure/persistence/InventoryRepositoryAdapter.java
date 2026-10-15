package com.vaultgame.inventory.infrastructure.persistence;

import com.vaultgame.inventory.domain.model.InventoryItem;
import com.vaultgame.inventory.domain.repository.InventoryRepository;
import com.vaultgame.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class InventoryRepositoryAdapter implements InventoryRepository {

    private final SpringDataInventoryRepository springDataInventoryRepository;

    public InventoryRepositoryAdapter(SpringDataInventoryRepository springDataInventoryRepository) {
        this.springDataInventoryRepository = springDataInventoryRepository;
    }

    @Override
    public Optional<InventoryItem> findByProductId(String productId) {
        return springDataInventoryRepository.findById(productId).map(PersistenceMapper::toDomain);
    }

    @Override
    public List<InventoryItem> findByProductIds(Collection<String> productIds) {
        return springDataInventoryRepository.findByProductIdIn(productIds).stream()
                .map(PersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public InventoryItem save(InventoryItem item) {
        InventoryItemEntity saved = springDataInventoryRepository.save(PersistenceMapper.toEntity(item));
        return PersistenceMapper.toDomain(saved);
    }
}
