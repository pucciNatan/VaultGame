package com.vaultgame.customer.domain.exception;

public class AddressLimitExceededException extends RuntimeException {

    public AddressLimitExceededException(int limit) {
        super("Address limit exceeded: max " + limit);
    }
}
