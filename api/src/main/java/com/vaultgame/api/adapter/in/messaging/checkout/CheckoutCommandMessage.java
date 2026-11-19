package com.vaultgame.api.adapter.in.messaging.checkout;

import java.util.UUID;

public record CheckoutCommandMessage(
        UUID correlationId,
        String replyTopic,
        CheckoutCommandType type,
        Object payload
) {
}