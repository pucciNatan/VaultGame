package com.vaultgame.inventory.infrastructure.config;

import com.vaultgame.inventory.application.service.InventoryReservationFacade;
import com.vaultgame.inventory.application.service.ReservationCoordinator;
import com.vaultgame.inventory.domain.model.ReservationLine;
import com.vaultgame.inventory.domain.model.StockReservation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class TransactionalInventoryReservationFacade implements ReservationCoordinator {

    private final InventoryReservationFacade delegate;

    public TransactionalInventoryReservationFacade(InventoryReservationFacade delegate) {
        this.delegate = delegate;
    }

    @Override
    @Transactional
    public StockReservation reserve(String reference, List<ReservationLine> lines) {
        return delegate.reserve(reference, lines);
    }

    @Override
    @Transactional
    public StockReservation commit(UUID reservationId) {
        return delegate.commit(reservationId);
    }

    @Override
    @Transactional
    public StockReservation release(UUID reservationId) {
        return delegate.release(reservationId);
    }
}
