package com.vaultgame.email.infrastructure.web.dto;

public record SendEmailResponse(String status, String to, String template) {
}
