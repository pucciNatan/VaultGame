package com.vaultgame.order.domain.port;

import com.vaultgame.order.domain.port.model.ReservationRequestLine;
import com.vaultgame.order.domain.port.model.StockReservationResult;

import java.util.List;
import java.util.UUID;

public interface InventoryPort {

    StockReservationResult reserve(String reference, List<ReservationRequestLine> lines);

    void commit(UUID reservationId);

    void release(UUID reservationId);
}
