package com.vaultgame.customer.domain.address;

public class AddressLimitExceededException extends RuntimeException {

    public AddressLimitExceededException(int limit) {
        super("Address limit exceeded: max " + limit);
    }
}
