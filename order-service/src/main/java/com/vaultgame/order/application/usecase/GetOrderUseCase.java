package com.vaultgame.order.application.usecase;

import com.vaultgame.order.domain.model.order.Order;
import com.vaultgame.order.domain.exception.OrderAccessDeniedException;
import com.vaultgame.order.domain.exception.OrderNotFoundException;
import com.vaultgame.order.application.port.in.GetOrderPort;
import com.vaultgame.order.application.port.out.OrderRepositoryPort;

import java.util.List;
import java.util.UUID;

public class GetOrderUseCase implements GetOrderPort {

    private final OrderRepositoryPort orderRepositoryPort;

    public GetOrderUseCase(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    public Order getById(UUID customerId, UUID orderId) {
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        if (!order.customerId().equals(customerId)) {
            throw new OrderAccessDeniedException(orderId);
        }
        return order;
    }

    @Override
    public List<Order> listForCustomer(UUID customerId, int limit) {
        return orderRepositoryPort.findByCustomerId(customerId, limit);
    }
}
