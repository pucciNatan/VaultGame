package com.vaultgame.email.adapter.in.messaging;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryProcessedEventStore implements ProcessedEventStore {

    private final Set<String> processed = ConcurrentHashMap.newKeySet();

    @Override
    public boolean markProcessedIfNew(UUID eventId) {
        return processed.add(eventId.toString());
    }
}
