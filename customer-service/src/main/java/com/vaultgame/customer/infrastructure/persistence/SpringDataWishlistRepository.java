package com.vaultgame.customer.infrastructure.persistence;

import com.vaultgame.customer.infrastructure.persistence.entity.WishlistItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataWishlistRepository extends JpaRepository<WishlistItemEntity, WishlistItemEntity.WishlistItemId> {

    List<WishlistItemEntity> findByCustomerIdOrderByAddedAtDesc(UUID customerId);

    Optional<WishlistItemEntity> findByCustomerIdAndProductId(UUID customerId, String productId);
}
