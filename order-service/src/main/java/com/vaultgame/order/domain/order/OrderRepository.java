package com.vaultgame.order.domain.order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    Optional<Order> findByIdempotencyKey(String idempotencyKey);

    List<Order> findByCustomerId(UUID customerId, int limit);
}
