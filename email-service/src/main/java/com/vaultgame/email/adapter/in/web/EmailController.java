package com.vaultgame.email.adapter.in.web;

import com.vaultgame.email.adapter.in.web.dto.SendEmailRequest;
import com.vaultgame.email.adapter.in.web.dto.SendEmailResponse;
import com.vaultgame.email.application.port.in.SendTemplatedEmailPort;
import com.vaultgame.email.application.usecase.SendEmailCommand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final SendTemplatedEmailPort sendTemplatedEmailPort;

    public EmailController(SendTemplatedEmailPort sendTemplatedEmailPort) {
        this.sendTemplatedEmailPort = sendTemplatedEmailPort;
    }

    @PostMapping("/send")
    public ResponseEntity<SendEmailResponse> send(@Valid @RequestBody SendEmailRequest request) {
        sendTemplatedEmailPort.send(new SendEmailCommand(
                request.template(),
                request.to(),
                request.subject(),
                request.variables()));
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new SendEmailResponse("SENT", request.to(), request.template().name()));
    }
}
