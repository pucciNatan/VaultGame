package com.vaultgame.inventory.application.service;

import com.vaultgame.inventory.domain.model.ReservationLine;

import java.util.List;

public record ReserveStockCommand(String reference, List<ReservationLine> lines) {
}
