package com.vaultgame.order.domain.exception;

public class DownstreamServiceException extends RuntimeException {

    public DownstreamServiceException(String service, String message) {
        super(service + ": " + message);
    }
}
