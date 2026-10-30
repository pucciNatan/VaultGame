package com.vaultgame.inventory.application.usecase;

import com.vaultgame.inventory.application.port.out.InventoryRepositoryPort;
import com.vaultgame.inventory.application.port.out.ReservationRepositoryPort;
import com.vaultgame.inventory.domain.exception.InsufficientStockException;
import com.vaultgame.inventory.domain.exception.InvalidReservationStateException;
import com.vaultgame.inventory.domain.exception.ProductInventoryNotFoundException;
import com.vaultgame.inventory.domain.exception.ReservationNotFoundException;
import com.vaultgame.inventory.domain.model.InventoryItem;
import com.vaultgame.inventory.domain.model.ReservationLine;
import com.vaultgame.inventory.domain.model.ReservationStatus;
import com.vaultgame.inventory.domain.model.StockReservation;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Coordinates inventory mutations and reservation persistence in a single transaction
 * (implemented by the Spring adapter in config).
 */
public class InventoryReservationFacade {

    private final InventoryRepositoryPort inventoryRepository;
    private final ReservationRepositoryPort reservationRepository;
    private final long reservationTtlMinutes;

    public InventoryReservationFacade(
            InventoryRepositoryPort inventoryRepository,
            ReservationRepositoryPort reservationRepository,
            long reservationTtlMinutes
    ) {
        this.inventoryRepository = inventoryRepository;
        this.reservationRepository = reservationRepository;
        this.reservationTtlMinutes = reservationTtlMinutes;
    }

    public StockReservation reserve(String reference, List<ReservationLine> lines) {
        Map<String, Integer> quantities = aggregateQuantities(lines);
        Map<String, InventoryItem> items = loadItems(quantities.keySet());

        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            InventoryItem item = items.get(entry.getKey());
            if (item.availableQuantity() < entry.getValue()) {
                throw new InsufficientStockException(entry.getKey());
            }
        }

        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            InventoryItem item = items.get(entry.getKey());
            InventoryItem updated = item.withAvailableQuantity(
                    item.availableQuantity() - entry.getValue(),
                    item.version()
            );
            inventoryRepository.save(updated);
        }

        Instant expiresAt = Instant.now().plusSeconds(reservationTtlMinutes * 60);
        StockReservation reservation = StockReservation.createActive(reference, lines, expiresAt);
        return reservationRepository.save(reservation);
    }

    public StockReservation commit(UUID reservationId) {
        StockReservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (reservation.status() == ReservationStatus.COMMITTED) {
            return reservation;
        }
        if (reservation.status() == ReservationStatus.RELEASED) {
            throw new InvalidReservationStateException("Cannot commit a released reservation");
        }

        return reservationRepository.save(reservation.withStatus(ReservationStatus.COMMITTED));
    }

    public StockReservation release(UUID reservationId) {
        StockReservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (reservation.status() == ReservationStatus.RELEASED) {
            return reservation;
        }
        if (reservation.status() == ReservationStatus.COMMITTED) {
            throw new InvalidReservationStateException("Cannot release a committed reservation");
        }

        Map<String, Integer> quantities = aggregateLines(reservation.lines());
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            InventoryItem item = inventoryRepository.findByProductId(entry.getKey())
                    .orElseThrow(() -> new ProductInventoryNotFoundException(entry.getKey()));
            inventoryRepository.save(item.withAvailableQuantity(
                    item.availableQuantity() + entry.getValue(),
                    item.version()
            ));
        }

        return reservationRepository.save(reservation.withStatus(ReservationStatus.RELEASED));
    }

    private Map<String, InventoryItem> loadItems(Iterable<String> productIds) {
        Map<String, InventoryItem> items = new HashMap<>();
        for (String productId : productIds) {
            InventoryItem item = inventoryRepository.findByProductId(productId)
                    .orElseThrow(() -> new ProductInventoryNotFoundException(productId));
            items.put(productId, item);
        }
        return items;
    }

    private static Map<String, Integer> aggregateQuantities(List<ReservationLine> lines) {
        Map<String, Integer> quantities = new HashMap<>();
        for (ReservationLine line : lines) {
            quantities.merge(line.productId(), line.quantity(), Integer::sum);
        }
        return quantities;
    }

    private static Map<String, Integer> aggregateLines(List<ReservationLine> lines) {
        return aggregateQuantities(lines);
    }
}
