package com.vaultgame.email.infrastructure.mail;

import com.vaultgame.email.domain.model.EmailTemplate;
import com.vaultgame.email.domain.port.EmailTemplateRendererPort;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
public class ThymeleafTemplateRenderer implements EmailTemplateRendererPort {

    private final TemplateEngine templateEngine;

    public ThymeleafTemplateRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String render(EmailTemplate template, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(template.templatePath(), context);
    }
}
