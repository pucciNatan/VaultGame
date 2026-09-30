package com.vaultgame.email.infrastructure.web;

import com.vaultgame.email.application.service.SendEmailCommand;
import com.vaultgame.email.application.service.SendTemplatedEmailService;
import com.vaultgame.email.infrastructure.web.dto.SendEmailRequest;
import com.vaultgame.email.infrastructure.web.dto.SendEmailResponse;
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

    private final SendTemplatedEmailService sendTemplatedEmailService;

    public EmailController(SendTemplatedEmailService sendTemplatedEmailService) {
        this.sendTemplatedEmailService = sendTemplatedEmailService;
    }

    @PostMapping("/send")
    public ResponseEntity<SendEmailResponse> send(@Valid @RequestBody SendEmailRequest request) {
        sendTemplatedEmailService.send(new SendEmailCommand(
                request.template(),
                request.to(),
                request.subject(),
                request.variables()
        ));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new SendEmailResponse("SENT", request.to(), request.template().name())
        );
    }
}
