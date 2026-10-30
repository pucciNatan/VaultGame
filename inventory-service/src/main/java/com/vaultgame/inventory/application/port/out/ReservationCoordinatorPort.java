package com.vaultgame.inventory.application.port.out;

import com.vaultgame.inventory.domain.model.ReservationLine;
import com.vaultgame.inventory.domain.model.StockReservation;

import java.util.List;
import java.util.UUID;

public interface ReservationCoordinatorPort {

    StockReservation reserve(String reference, List<ReservationLine> lines);

    StockReservation commit(UUID reservationId);

    StockReservation release(UUID reservationId);
}
