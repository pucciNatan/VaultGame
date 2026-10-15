package com.vaultgame.inventory.infrastructure.persistence;

import com.vaultgame.inventory.domain.model.InventoryItem;
import com.vaultgame.inventory.domain.model.ReservationLine;
import com.vaultgame.inventory.domain.model.ReservationStatus;
import com.vaultgame.inventory.domain.model.StockReservation;
import com.vaultgame.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import com.vaultgame.inventory.infrastructure.persistence.entity.StockReservationEntity;
import com.vaultgame.inventory.infrastructure.persistence.entity.StockReservationLineEntity;

import java.util.List;

final class PersistenceMapper {

    private PersistenceMapper() {
    }

    static InventoryItem toDomain(InventoryItemEntity entity) {
        return new InventoryItem(
                entity.getProductId(),
                entity.getAvailableQuantity(),
                entity.getVersion(),
                entity.getUpdatedAt()
        );
    }

    static InventoryItemEntity toEntity(InventoryItem item) {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setProductId(item.productId());
        entity.setAvailableQuantity(item.availableQuantity());
        entity.setVersion(item.version());
        entity.setUpdatedAt(item.updatedAt());
        return entity;
    }

    static StockReservation toDomain(StockReservationEntity entity) {
        List<ReservationLine> lines = entity.getLines().stream()
                .map(line -> new ReservationLine(line.getProductId(), line.getQuantity()))
                .toList();
        return new StockReservation(
                entity.getId(),
                entity.getReference(),
                ReservationStatus.valueOf(entity.getStatus()),
                lines,
                entity.getExpiresAt(),
                entity.getCreatedAt()
        );
    }

    static StockReservationEntity toEntity(StockReservation reservation) {
        StockReservationEntity entity = new StockReservationEntity();
        entity.setId(reservation.id());
        entity.setReference(reservation.reference());
        entity.setStatus(reservation.status().name());
        entity.setExpiresAt(reservation.expiresAt());
        entity.setCreatedAt(reservation.createdAt());

        for (ReservationLine line : reservation.lines()) {
            StockReservationLineEntity lineEntity = new StockReservationLineEntity();
            lineEntity.setReservationId(reservation.id());
            lineEntity.setProductId(line.productId());
            lineEntity.setQuantity(line.quantity());
            lineEntity.setReservation(entity);
            entity.getLines().add(lineEntity);
        }
        return entity;
    }
}
