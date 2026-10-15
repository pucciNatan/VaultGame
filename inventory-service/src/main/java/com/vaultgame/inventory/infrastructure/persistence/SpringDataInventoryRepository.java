package com.vaultgame.inventory.infrastructure.persistence;

import com.vaultgame.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SpringDataInventoryRepository extends JpaRepository<InventoryItemEntity, String> {

    List<InventoryItemEntity> findByProductIdIn(Collection<String> productIds);
}
