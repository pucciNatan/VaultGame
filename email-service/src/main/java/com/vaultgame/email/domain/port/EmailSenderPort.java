package com.vaultgame.email.domain.port;

import com.vaultgame.email.domain.model.EmailMessage;

public interface EmailSenderPort {

    void send(EmailMessage message);
}
