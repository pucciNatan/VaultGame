package com.vaultgame.email.application.service;

import com.vaultgame.email.domain.model.EmailMessage;
import com.vaultgame.email.domain.port.EmailSenderPort;
import com.vaultgame.email.domain.port.EmailTemplateRendererPort;

import java.util.HashMap;
import java.util.Map;

public class SendTemplatedEmailService {

    private final EmailTemplateRendererPort templateRenderer;
    private final EmailSenderPort emailSender;

    public SendTemplatedEmailService(EmailTemplateRendererPort templateRenderer, EmailSenderPort emailSender) {
        this.templateRenderer = templateRenderer;
        this.emailSender = emailSender;
    }

    public void send(SendEmailCommand command) {
        Map<String, Object> variables = command.variables() != null
                ? new HashMap<>(command.variables())
                : new HashMap<>();

        String htmlBody = templateRenderer.render(command.template(), variables);
        String subject = command.subject() != null && !command.subject().isBlank()
                ? command.subject().trim()
                : command.template().defaultSubject();

        EmailMessage message = new EmailMessage(command.to().trim(), subject, htmlBody);
        emailSender.send(message);
    }
}
