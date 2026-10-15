package com.vaultgame.customer.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "wishlist_items")
@IdClass(WishlistItemEntity.WishlistItemId.class)
public class WishlistItemEntity {

    @Id
    @Column(name = "customer_id")
    private UUID customerId;

    @Id
    @Column(name = "product_id", length = 64)
    private String productId;

    @Column(name = "added_at", nullable = false)
    private Instant addedAt;

    protected WishlistItemEntity() {
    }

    public WishlistItemEntity(UUID customerId, String productId, Instant addedAt) {
        this.customerId = customerId;
        this.productId = productId;
        this.addedAt = addedAt;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getProductId() {
        return productId;
    }

    public Instant getAddedAt() {
        return addedAt;
    }

    public static class WishlistItemId implements Serializable {
        private UUID customerId;
        private String productId;

        public WishlistItemId() {
        }

        public WishlistItemId(UUID customerId, String productId) {
            this.customerId = customerId;
            this.productId = productId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            WishlistItemId that = (WishlistItemId) o;
            return Objects.equals(customerId, that.customerId) && Objects.equals(productId, that.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customerId, productId);
        }
    }
}
