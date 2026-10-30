package com.vaultgame.inventory.adapter.out.persistence.repository;

import com.vaultgame.inventory.adapter.out.persistence.entity.StockReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataReservationRepository extends JpaRepository<StockReservationEntity, UUID> {
}
