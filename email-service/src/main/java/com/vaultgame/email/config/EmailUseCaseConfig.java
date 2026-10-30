package com.vaultgame.email.config;

import com.vaultgame.email.application.port.in.SendTemplatedEmailPort;
import com.vaultgame.email.application.port.out.EmailSenderPort;
import com.vaultgame.email.application.port.out.EmailTemplateRendererPort;
import com.vaultgame.email.application.usecase.SendTemplatedEmailUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailUseCaseConfig {

    @Bean
    SendTemplatedEmailPort sendTemplatedEmailPort(
            EmailTemplateRendererPort templateRenderer,
            EmailSenderPort emailSender) {
        return new SendTemplatedEmailUseCase(templateRenderer, emailSender);
    }
}
