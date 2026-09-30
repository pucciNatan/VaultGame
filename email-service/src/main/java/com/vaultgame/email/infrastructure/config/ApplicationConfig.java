package com.vaultgame.email.infrastructure.config;

import com.vaultgame.email.application.service.SendTemplatedEmailService;
import com.vaultgame.email.domain.port.EmailSenderPort;
import com.vaultgame.email.domain.port.EmailTemplateRendererPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    SendTemplatedEmailService sendTemplatedEmailService(
            EmailTemplateRendererPort templateRenderer,
            EmailSenderPort emailSender
    ) {
        return new SendTemplatedEmailService(templateRenderer, emailSender);
    }
}
