package com.vaultgame.inventory.infrastructure.persistence;

import com.vaultgame.inventory.infrastructure.persistence.entity.StockReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataReservationRepository extends JpaRepository<StockReservationEntity, UUID> {
}
