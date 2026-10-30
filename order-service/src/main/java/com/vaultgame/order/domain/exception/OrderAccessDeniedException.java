package com.vaultgame.order.domain.exception;

import java.util.UUID;

public class OrderAccessDeniedException extends RuntimeException {

    public OrderAccessDeniedException(UUID orderId) {
        super("Access denied to order: " + orderId);
    }
}
