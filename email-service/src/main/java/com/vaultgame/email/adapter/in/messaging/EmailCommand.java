package com.vaultgame.email.adapter.in.messaging;

import java.util.Map;

/**
 * Contract for a future Kafka consumer on topic {@code vaultgame.email.send}.
 * A {@code @KafkaListener} will deserialize to this record and delegate to {@code SendTemplatedEmailPort}.
 */
public record EmailCommand(String template, String to, String subject, Map<String, Object> variables) {
}
