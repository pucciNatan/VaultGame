package com.vaultgame.inventory.domain.repository;

import com.vaultgame.inventory.domain.model.StockReservation;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {

    StockReservation save(StockReservation reservation);

    Optional<StockReservation> findById(UUID id);
}
