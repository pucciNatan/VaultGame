package com.vaultgame.email.infrastructure.web.dto;

import com.vaultgame.email.domain.model.EmailTemplate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record SendEmailRequest(
        @NotNull EmailTemplate template,
        @NotBlank @Email String to,
        String subject,
        Map<String, Object> variables
) {
}
