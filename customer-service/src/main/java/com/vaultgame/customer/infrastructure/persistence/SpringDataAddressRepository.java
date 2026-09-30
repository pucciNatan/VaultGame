package com.vaultgame.customer.infrastructure.persistence;

import com.vaultgame.customer.infrastructure.persistence.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataAddressRepository extends JpaRepository<AddressEntity, UUID> {

    List<AddressEntity> findByCustomerIdOrderByCreatedAtAsc(UUID customerId);

    Optional<AddressEntity> findByIdAndCustomerId(UUID id, UUID customerId);

    long countByCustomerId(UUID customerId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE AddressEntity a SET a.isDefault = false WHERE a.customerId = :customerId")
    void clearDefaultForCustomer(@Param("customerId") UUID customerId);
}
