package com.vaultgame.order.adapter.out.persistence;

import com.vaultgame.order.domain.model.order.Order;
import com.vaultgame.order.domain.model.order.OrderLine;
import com.vaultgame.order.domain.model.order.OrderStatus;
import com.vaultgame.order.adapter.out.persistence.entity.OrderEntity;
import com.vaultgame.order.adapter.out.persistence.entity.OrderLineEntity;

import java.util.List;

final class PersistenceMapper {

    private PersistenceMapper() {
    }

    static Order toDomain(OrderEntity entity) {
        List<OrderLine> lines = entity.getLines().stream()
                .map(line -> new OrderLine(
                        line.getId(),
                        line.getProductId(),
                        line.getProductName(),
                        line.getUnitPrice(),
                        line.getQuantity(),
                        line.getLineTotal()
                ))
                .toList();
        return new Order(
                entity.getId(),
                entity.getCustomerId(),
                entity.getCartKey(),
                OrderStatus.valueOf(entity.getStatus()),
                entity.getCurrency(),
                entity.getTotalAmount(),
                entity.getPaymentTransactionId(),
                entity.getReservationId(),
                entity.getShippingAddressId(),
                entity.getIdempotencyKey(),
                entity.getRequestHash(),
                lines,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    static OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.id());
        entity.setCustomerId(order.customerId());
        entity.setCartKey(order.cartKey());
        entity.setStatus(order.status().name());
        entity.setCurrency(order.currency());
        entity.setTotalAmount(order.totalAmount());
        entity.setPaymentTransactionId(order.paymentTransactionId());
        entity.setReservationId(order.reservationId());
        entity.setShippingAddressId(order.shippingAddressId());
        entity.setIdempotencyKey(order.idempotencyKey());
        entity.setRequestHash(order.requestHash());
        entity.setCreatedAt(order.createdAt());
        entity.setUpdatedAt(order.updatedAt());

        for (OrderLine line : order.lines()) {
            OrderLineEntity lineEntity = new OrderLineEntity();
            lineEntity.setId(line.id());
            lineEntity.setOrder(entity);
            lineEntity.setProductId(line.productId());
            lineEntity.setProductName(line.productName());
            lineEntity.setUnitPrice(line.unitPrice());
            lineEntity.setQuantity(line.quantity());
            lineEntity.setLineTotal(line.lineTotal());
            entity.getLines().add(lineEntity);
        }
        return entity;
    }
}
