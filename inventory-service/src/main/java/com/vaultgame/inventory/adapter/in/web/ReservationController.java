package com.vaultgame.inventory.adapter.in.web;

import com.vaultgame.inventory.adapter.in.web.dto.InventoryDtos;
import com.vaultgame.inventory.application.port.in.ReserveStockPort;
import com.vaultgame.inventory.domain.model.ReservationLine;
import com.vaultgame.inventory.domain.model.StockReservation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/reservations")
public class ReservationController {

    private final ReserveStockPort reserveStockPort;

    public ReservationController(ReserveStockPort reserveStockPort) {
        this.reserveStockPort = reserveStockPort;
    }

    @PostMapping
    public ResponseEntity<InventoryDtos.ReservationResponse> create(
            @Valid @RequestBody InventoryDtos.CreateReservationRequest request
    ) {
        List<ReservationLine> lines = request.lines().stream()
                .map(line -> new ReservationLine(line.productId(), line.quantity()))
                .toList();
        StockReservation reservation = reserveStockPort.reserve(request.reference(), lines);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(reservation));
    }

    @PostMapping("/{reservationId}/commit")
    public InventoryDtos.ReservationResponse commit(@PathVariable UUID reservationId) {
        return toResponse(reserveStockPort.commit(reservationId));
    }

    @PostMapping("/{reservationId}/release")
    public InventoryDtos.ReservationResponse release(@PathVariable UUID reservationId) {
        return toResponse(reserveStockPort.release(reservationId));
    }

    private static InventoryDtos.ReservationResponse toResponse(StockReservation reservation) {
        List<InventoryDtos.ReservationLineResponse> lines = reservation.lines().stream()
                .map(line -> new InventoryDtos.ReservationLineResponse(line.productId(), line.quantity()))
                .toList();
        return new InventoryDtos.ReservationResponse(
                reservation.id().toString(),
                reservation.reference(),
                reservation.status().name(),
                lines,
                reservation.expiresAt().toString()
        );
    }
}
