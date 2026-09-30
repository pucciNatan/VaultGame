package com.vaultgame.email.infrastructure.mail;

import com.vaultgame.email.domain.exception.EmailSendException;
import com.vaultgame.email.domain.model.EmailMessage;
import com.vaultgame.email.domain.port.EmailSenderPort;
import com.vaultgame.email.infrastructure.config.MailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class JavaMailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    public JavaMailSenderAdapter(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    @Override
    public void send(EmailMessage message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
            helper.setFrom(mailProperties.from());
            helper.setTo(message.to());
            helper.setSubject(message.subject());
            helper.setText(message.htmlBody(), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            throw new EmailSendException("Failed to send email", ex);
        }
    }
}
