package com.vaultgame.inventory.adapter.out.persistence.repository;

import com.vaultgame.inventory.adapter.out.persistence.entity.InventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SpringDataInventoryRepository extends JpaRepository<InventoryItemEntity, String> {

    List<InventoryItemEntity> findByProductIdIn(Collection<String> productIds);
}
