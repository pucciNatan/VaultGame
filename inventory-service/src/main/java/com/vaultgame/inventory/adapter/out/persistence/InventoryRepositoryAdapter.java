package com.vaultgame.inventory.adapter.out.persistence;

import com.vaultgame.inventory.adapter.out.persistence.entity.InventoryItemEntity;
import com.vaultgame.inventory.adapter.out.persistence.repository.SpringDataInventoryRepository;
import com.vaultgame.inventory.application.port.out.InventoryRepositoryPort;
import com.vaultgame.inventory.domain.model.InventoryItem;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class InventoryRepositoryAdapter implements InventoryRepositoryPort {

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
