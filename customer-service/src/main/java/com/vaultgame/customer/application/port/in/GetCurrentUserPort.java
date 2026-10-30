package com.vaultgame.customer.application.port.in;

import com.vaultgame.customer.domain.model.identity.User;
import java.util.UUID;

public interface GetCurrentUserPort {
    User execute(UUID userId);
}
