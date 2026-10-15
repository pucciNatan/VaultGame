package com.vaultgame.order.application.order;

import com.vaultgame.order.domain.order.Order;
import com.vaultgame.order.domain.order.OrderAccessDeniedException;
import com.vaultgame.order.domain.order.OrderNotFoundException;
import com.vaultgame.order.domain.order.OrderRepository;

import java.util.List;
import java.util.UUID;

public class GetOrderService {

    private final OrderRepository orderRepository;

    public GetOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order getById(UUID customerId, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        if (!order.customerId().equals(customerId)) {
            throw new OrderAccessDeniedException(orderId);
        }
        return order;
    }

    public List<Order> listForCustomer(UUID customerId, int limit) {
        return orderRepository.findByCustomerId(customerId, limit);
    }
}
