package com.vaultgame.email.domain.exception;

public class UnknownTemplateException extends RuntimeException {

    public UnknownTemplateException(String template) {
        super("Unknown email template: " + template);
    }
}
