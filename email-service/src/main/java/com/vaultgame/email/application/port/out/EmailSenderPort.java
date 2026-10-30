package com.vaultgame.email.application.port.out;

import com.vaultgame.email.domain.model.EmailMessage;

public interface EmailSenderPort {

    void send(EmailMessage message);
}
