package com.vaultgame.customer.application.port.in;

import com.vaultgame.customer.application.usecase.customer.UpdateProfileCommand;
import com.vaultgame.customer.domain.model.customer.Customer;
import java.util.UUID;

public interface UpdateProfilePort {
    Customer execute(UUID customerId, UpdateProfileCommand command);
}
