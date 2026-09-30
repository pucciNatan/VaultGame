package com.vaultgame.fakepaymentgateway.domain.exception;

public class PaymentTimeoutException extends RuntimeException {

    public PaymentTimeoutException(String message) {
        super(message);
    }
}
