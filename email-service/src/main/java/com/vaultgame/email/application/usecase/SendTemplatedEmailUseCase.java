package com.vaultgame.email.application.usecase;

import com.vaultgame.email.application.port.in.SendTemplatedEmailPort;
import com.vaultgame.email.application.port.out.EmailSenderPort;
import com.vaultgame.email.application.port.out.EmailTemplateRendererPort;
import com.vaultgame.email.domain.model.EmailMessage;
import java.util.HashMap;
import java.util.Map;

public class SendTemplatedEmailUseCase implements SendTemplatedEmailPort {

    private final EmailTemplateRendererPort templateRenderer;
    private final EmailSenderPort emailSender;

    public SendTemplatedEmailUseCase(EmailTemplateRendererPort templateRenderer, EmailSenderPort emailSender) {
        this.templateRenderer = templateRenderer;
        this.emailSender = emailSender;
    }

    @Override
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
