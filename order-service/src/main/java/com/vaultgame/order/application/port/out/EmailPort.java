package com.vaultgame.order.application.port.out;

import java.util.Map;
import java.util.UUID;

public interface EmailPort {

    void sendOrderConfirmed(String to, UUID orderId, String fullName);
}
