package com.vaultgame.fakepaymentgateway.infrastructure.web;

import com.vaultgame.fakepaymentgateway.application.service.CreatePaymentCommand;
import com.vaultgame.fakepaymentgateway.application.service.PaymentService;
import com.vaultgame.fakepaymentgateway.domain.model.Payment;
import com.vaultgame.fakepaymentgateway.infrastructure.web.dto.CreatePaymentRequest;
import com.vaultgame.fakepaymentgateway.infrastructure.web.dto.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ) {
        CreatePaymentCommand command = new CreatePaymentCommand(
                request.reference(),
                request.amount(),
                request.currency(),
                request.paymentMethod().type(),
                request.paymentMethod().token(),
                idempotencyKey
        );
        Payment payment = paymentService.createPayment(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(PaymentResponse.from(payment));
    }

    @GetMapping("/{transactionId}")
    public PaymentResponse getPayment(@PathVariable String transactionId) {
        Payment payment = paymentService.getPayment(transactionId);
        return PaymentResponse.from(payment);
    }
}
