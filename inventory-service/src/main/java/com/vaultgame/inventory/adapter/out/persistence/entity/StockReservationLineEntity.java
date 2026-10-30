package com.vaultgame.inventory.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "stock_reservation_lines")
@IdClass(StockReservationLineEntity.LineId.class)
public class StockReservationLineEntity {

    @Id
    @Column(name = "reservation_id")
    private UUID reservationId;

    @Id
    @Column(name = "product_id", length = 64)
    private String productId;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", insertable = false, updatable = false)
    private StockReservationEntity reservation;

    public UUID getReservationId() {
        return reservationId;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public StockReservationEntity getReservation() {
        return reservation;
    }

    public void setReservation(StockReservationEntity reservation) {
        this.reservation = reservation;
    }

    public static class LineId implements Serializable {
        private UUID reservationId;
        private String productId;

        public LineId() {
        }

        public LineId(UUID reservationId, String productId) {
            this.reservationId = reservationId;
            this.productId = productId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            LineId lineId = (LineId) o;
            return Objects.equals(reservationId, lineId.reservationId)
                    && Objects.equals(productId, lineId.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reservationId, productId);
        }
    }
}
