package com.vaultgame.customer.application.port.in;

import com.vaultgame.customer.domain.model.customer.Customer;
import java.util.UUID;

public interface GetProfilePort {
    Customer execute(UUID customerId);
}
