package com.vaultgame.order.domain.port;

import com.vaultgame.order.domain.port.model.CustomerProfile;

import java.util.UUID;

public interface CustomerPort {

    CustomerProfile getProfile(UUID customerId, String bearerToken);
}
