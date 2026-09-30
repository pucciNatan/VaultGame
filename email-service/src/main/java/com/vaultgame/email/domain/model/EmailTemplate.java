package com.vaultgame.email.domain.model;

public enum EmailTemplate {
    WELCOME("email/welcome", "Welcome to VaultGame"),
    ORDER_CONFIRMED("email/order-confirmed", "Your order was confirmed");

    private final String templatePath;
    private final String defaultSubject;

    EmailTemplate(String templatePath, String defaultSubject) {
        this.templatePath = templatePath;
        this.defaultSubject = defaultSubject;
    }

    public String templatePath() {
        return templatePath;
    }

    public String defaultSubject() {
        return defaultSubject;
    }
}
