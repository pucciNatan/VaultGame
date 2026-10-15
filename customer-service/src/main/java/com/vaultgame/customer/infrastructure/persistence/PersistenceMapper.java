package com.vaultgame.customer.infrastructure.persistence;

import com.vaultgame.customer.domain.address.Address;
import com.vaultgame.customer.domain.customer.Customer;
import com.vaultgame.customer.domain.identity.User;
import com.vaultgame.customer.domain.wishlist.WishlistItem;
import com.vaultgame.customer.infrastructure.persistence.entity.AddressEntity;
import com.vaultgame.customer.infrastructure.persistence.entity.CustomerEntity;
import com.vaultgame.customer.infrastructure.persistence.entity.UserEntity;
import com.vaultgame.customer.infrastructure.persistence.entity.WishlistItemEntity;

final class PersistenceMapper {

    private PersistenceMapper() {
    }

    static UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.email(),
                user.passwordHash(),
                user.role(),
                user.enabled(),
                user.createdAt(),
                user.updatedAt()
        );
    }

    static User toDomain(UserEntity entity) {
        return User.rehydrate(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole(),
                entity.isEnabled(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    static CustomerEntity toEntity(Customer customer) {
        return new CustomerEntity(
                customer.id(),
                customer.email(),
                customer.fullName(),
                customer.phone(),
                customer.createdAt(),
                customer.updatedAt()
        );
    }

    static Customer toDomain(CustomerEntity entity) {
        return Customer.rehydrate(
                entity.getId(),
                entity.getEmail(),
                entity.getFullName(),
                entity.getPhone(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    static AddressEntity toEntity(Address address) {
        return new AddressEntity(
                address.id(),
                address.customerId(),
                address.label(),
                address.street(),
                address.number(),
                address.complement(),
                address.district(),
                address.city(),
                address.state(),
                address.postalCode(),
                address.country(),
                address.type(),
                address.isDefault(),
                address.createdAt(),
                address.updatedAt()
        );
    }

    static Address toDomain(AddressEntity entity) {
        return Address.rehydrate(
                entity.getId(),
                entity.getCustomerId(),
                entity.getLabel(),
                entity.getStreet(),
                entity.getNumber(),
                entity.getComplement(),
                entity.getDistrict(),
                entity.getCity(),
                entity.getState(),
                entity.getPostalCode(),
                entity.getCountry(),
                entity.getType(),
                entity.isDefault(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    static WishlistItemEntity toEntity(WishlistItem item) {
        return new WishlistItemEntity(item.customerId(), item.productId(), item.addedAt());
    }

    static WishlistItem toDomain(WishlistItemEntity entity) {
        return WishlistItem.rehydrate(entity.getCustomerId(), entity.getProductId(), entity.getAddedAt());
    }
}
