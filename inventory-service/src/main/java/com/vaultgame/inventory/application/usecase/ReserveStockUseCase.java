package com.vaultgame.inventory.application.usecase;

import com.vaultgame.inventory.application.port.in.ReserveStockPort;
import com.vaultgame.inventory.application.port.out.ReservationCoordinatorPort;
import com.vaultgame.inventory.domain.model.ReservationLine;
import com.vaultgame.inventory.domain.model.StockReservation;

import java.util.List;
import java.util.UUID;

public class ReserveStockUseCase implements ReserveStockPort {

    private final ReservationCoordinatorPort reservationCoordinator;

    public ReserveStockUseCase(ReservationCoordinatorPort reservationCoordinator) {
        this.reservationCoordinator = reservationCoordinator;
    }

    @Override
    public StockReservation reserve(String reference, List<ReservationLine> lines) {
        return reservationCoordinator.reserve(reference, lines);
    }

    @Override
    public StockReservation commit(UUID reservationId) {
        return reservationCoordinator.commit(reservationId);
    }

    @Override
    public StockReservation release(UUID reservationId) {
        return reservationCoordinator.release(reservationId);
    }
}
