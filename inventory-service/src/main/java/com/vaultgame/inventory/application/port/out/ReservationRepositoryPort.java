package com.vaultgame.inventory.application.port.out;

import com.vaultgame.inventory.domain.model.StockReservation;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepositoryPort {

    StockReservation save(StockReservation reservation);

    Optional<StockReservation> findById(UUID id);
}
