package com.vaultgame.email.application.port.in;

import com.vaultgame.email.application.usecase.SendEmailCommand;

public interface SendTemplatedEmailPort {

    void send(SendEmailCommand command);
}
