package com.vaultgame.order.application.port.out;

import com.vaultgame.order.application.port.out.model.CustomerProfile;

import java.util.UUID;

public interface CustomerPort {

    CustomerProfile getProfile(UUID customerId, String bearerToken);
}
