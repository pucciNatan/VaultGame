package com.vaultgame.order.application.port.out;

import java.math.BigDecimal;
import java.util.UUID;

public interface OrderEventPublisherPort {

    void publishPaid(
            UUID orderId,
            UUID customerId,
            String email,
            String fullName,
            BigDecimal total,
            String currency
    );
}
