package com.vaultgame.email.infrastructure.messaging;

import java.util.Map;

/**
 * Contract for a future Kafka consumer on topic {@code vaultgame.email.send}.
 * A {@code @KafkaListener} will deserialize to this record and delegate to {@code SendTemplatedEmailService}.
 */
public record EmailCommand(
        String template,
        String to,
        String subject,
        Map<String, Object> variables
) {
}
