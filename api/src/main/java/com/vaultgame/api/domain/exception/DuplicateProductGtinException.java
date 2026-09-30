package com.vaultgame.api.domain.exception;

public class DuplicateProductGtinException extends DomainException {

    public DuplicateProductGtinException(String gtin) {
        super("Product with GTIN already exists: " + gtin);
    }
}
