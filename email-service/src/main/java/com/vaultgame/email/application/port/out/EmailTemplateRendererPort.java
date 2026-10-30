package com.vaultgame.email.application.port.out;

import com.vaultgame.email.domain.model.EmailTemplate;
import java.util.Map;

public interface EmailTemplateRendererPort {

    String render(EmailTemplate template, Map<String, Object> variables);
}
