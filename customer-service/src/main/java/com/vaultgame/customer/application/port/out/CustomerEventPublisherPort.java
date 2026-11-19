package com.vaultgame.customer.application.port.out;

import java.util.UUID;

public interface CustomerEventPublisherPort {

    void publishRegistered(UUID customerId, String email, String fullName);
}
