package com.vaultgame.fakepaymentgateway.adapter.in.messaging.checkout;

import java.util.UUID;

public record CheckoutReplyMessage(
        UUID correlationId,
        boolean success,
        String errorCode,
        String errorMessage,
        Object data
) {
    public static CheckoutReplyMessage ok(UUID correlationId, Object data) {
        return new CheckoutReplyMessage(correlationId, true, null, null, data);
    }

    public static CheckoutReplyMessage failure(UUID correlationId, String errorCode, String errorMessage) {
        return new CheckoutReplyMessage(correlationId, false, errorCode, errorMessage, null);
    }
}