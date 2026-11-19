package com.vaultgame.email.adapter.in.messaging;

import java.util.Map;

/**
 * Manual REST payload for {@code POST /emails/send}. Domain events use {@link VaultGameEmailKafkaListeners}.
 */
public record EmailCommand(String template, String to, String subject, Map<String, Object> variables) {
}
