package com.vaultgame.order.application.port.out;

import com.vaultgame.order.application.port.out.model.ReservationRequestLine;
import com.vaultgame.order.application.port.out.model.StockReservationResult;

import java.util.List;
import java.util.UUID;

public interface InventoryPort {

    StockReservationResult reserve(String reference, List<ReservationRequestLine> lines);

    void commit(UUID reservationId);

    void release(UUID reservationId);
}
