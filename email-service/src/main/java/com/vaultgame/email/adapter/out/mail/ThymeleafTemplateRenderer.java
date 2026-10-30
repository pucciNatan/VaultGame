package com.vaultgame.email.adapter.out.mail;

import com.vaultgame.email.application.port.out.EmailTemplateRendererPort;
import com.vaultgame.email.domain.model.EmailTemplate;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
