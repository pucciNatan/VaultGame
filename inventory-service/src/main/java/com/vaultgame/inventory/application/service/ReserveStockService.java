package com.vaultgame.inventory.application.service;

import com.vaultgame.inventory.domain.model.StockReservation;

import java.util.UUID;

public class ReserveStockService {

    private final ReservationCoordinator reservationCoordinator;

    public ReserveStockService(ReservationCoordinator reservationCoordinator) {
        this.reservationCoordinator = reservationCoordinator;
    }

    public StockReservation reserve(ReserveStockCommand command) {
        return reservationCoordinator.reserve(command.reference(), command.lines());
    }

    public StockReservation commit(UUID reservationId) {
        return reservationCoordinator.commit(reservationId);
    }

    public StockReservation release(UUID reservationId) {
        return reservationCoordinator.release(reservationId);
    }
}
