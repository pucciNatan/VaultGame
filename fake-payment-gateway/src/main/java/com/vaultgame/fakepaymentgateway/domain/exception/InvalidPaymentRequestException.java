package com.vaultgame.fakepaymentgateway.domain.exception;

public class InvalidPaymentRequestException extends RuntimeException {

    private final String code;

    public InvalidPaymentRequestException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
