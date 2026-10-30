package com.vaultgame.order.application.port.out;

import com.vaultgame.order.domain.model.order.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    Optional<Order> findByIdempotencyKey(String idempotencyKey);

    List<Order> findByCustomerId(UUID customerId, int limit);
}
