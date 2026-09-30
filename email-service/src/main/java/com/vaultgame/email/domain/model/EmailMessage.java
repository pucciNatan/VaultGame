package com.vaultgame.email.domain.model;

import java.util.Objects;

public record EmailMessage(String to, String subject, String htmlBody) {

    public EmailMessage {
        Objects.requireNonNull(to, "to");
        Objects.requireNonNull(subject, "subject");
        Objects.requireNonNull(htmlBody, "htmlBody");
        if (to.isBlank()) {
            throw new IllegalArgumentException("to must not be blank");
        }
        if (subject.isBlank()) {
            throw new IllegalArgumentException("subject must not be blank");
        }
    }
}
