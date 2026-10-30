package com.vaultgame.email.application.usecase;

import com.vaultgame.email.domain.model.EmailTemplate;
import java.util.Map;

public record SendEmailCommand(
        EmailTemplate template,
        String to,
        String subject,
        Map<String, Object> variables) {
}
