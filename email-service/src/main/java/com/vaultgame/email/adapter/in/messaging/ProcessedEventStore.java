package com.vaultgame.email.adapter.in.messaging;

import java.util.UUID;

/**
 * In-memory deduplication by {@code eventId} (dev v1; not durable across restarts).
 */
public interface ProcessedEventStore {

    /**
     * @return {@code true} if this is the first time the event is seen
     */
    boolean markProcessedIfNew(UUID eventId);
}
