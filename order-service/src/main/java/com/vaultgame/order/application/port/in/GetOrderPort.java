package com.vaultgame.order.application.port.in;

import com.vaultgame.order.domain.model.order.Order;

import java.util.List;
import java.util.UUID;

public interface GetOrderPort {

    Order getById(UUID customerId, UUID orderId);

    List<Order> listForCustomer(UUID customerId, int limit);
}
