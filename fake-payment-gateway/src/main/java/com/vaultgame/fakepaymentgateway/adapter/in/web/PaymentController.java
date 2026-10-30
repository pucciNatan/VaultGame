package com.vaultgame.fakepaymentgateway.adapter.in.web;

import com.vaultgame.fakepaymentgateway.adapter.in.web.dto.CreatePaymentRequest;
import com.vaultgame.fakepaymentgateway.adapter.in.web.dto.PaymentResponse;
import com.vaultgame.fakepaymentgateway.application.port.in.CreatePaymentPort;
import com.vaultgame.fakepaymentgateway.application.port.in.GetPaymentPort;
import com.vaultgame.fakepaymentgateway.application.usecase.CreatePaymentCommand;
import com.vaultgame.fakepaymentgateway.domain.model.Payment;
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

    private final CreatePaymentPort createPaymentPort;
    private final GetPaymentPort getPaymentPort;

    public PaymentController(CreatePaymentPort createPaymentPort, GetPaymentPort getPaymentPort) {
        this.createPaymentPort = createPaymentPort;
        this.getPaymentPort = getPaymentPort;
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
        Payment payment = createPaymentPort.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(PaymentResponse.from(payment));
    }

    @GetMapping("/{transactionId}")
    public PaymentResponse getPayment(@PathVariable String transactionId) {
        Payment payment = getPaymentPort.execute(transactionId);
        return PaymentResponse.from(payment);
    }
}
