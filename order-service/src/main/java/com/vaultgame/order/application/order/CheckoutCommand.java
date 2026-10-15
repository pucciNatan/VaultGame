package com.vaultgame.order.application.order;

import java.util.UUID;

public record CheckoutCommand(
        UUID customerId,
        String cartKey,
        UUID shippingAddressId,
        String paymentMethodType,
        String paymentMethodToken,
        String idempotencyKey,
        String bearerToken
) {
}
