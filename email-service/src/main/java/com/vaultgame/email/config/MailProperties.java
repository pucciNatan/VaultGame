package com.vaultgame.email.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vaultgame.mail")
public record MailProperties(String from) {
}
