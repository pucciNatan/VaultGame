package com.vaultgame.order.application.port.in;

import com.vaultgame.order.application.usecase.CheckoutCommand;
import com.vaultgame.order.domain.model.order.Order;

public interface PlaceOrderPort {

    Order checkout(CheckoutCommand command);
}
