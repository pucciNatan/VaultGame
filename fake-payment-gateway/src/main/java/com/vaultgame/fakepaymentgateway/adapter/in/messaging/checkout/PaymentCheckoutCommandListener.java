package com.vaultgame.fakepaymentgateway.adapter.in.messaging.checkout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaultgame.fakepaymentgateway.application.port.in.CreatePaymentPort;
import com.vaultgame.fakepaymentgateway.application.usecase.CreatePaymentCommand;
import com.vaultgame.fakepaymentgateway.domain.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCheckoutCommandListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentCheckoutCommandListener.class);

    private final CreatePaymentPort createPaymentPort;
    private final CheckoutReplySender checkoutReplySender;
    private final ObjectMapper objectMapper;

    public PaymentCheckoutCommandListener(
            CreatePaymentPort createPaymentPort,
            CheckoutReplySender checkoutReplySender,
            ObjectMapper objectMapper
    ) {
        this.createPaymentPort = createPaymentPort;
        this.checkoutReplySender = checkoutReplySender;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = VaultGameCheckoutTopics.COMMANDS_PAYMENT,
            containerFactory = "checkoutCommandKafkaListenerContainerFactory"
    )
    public void onCommand(CheckoutCommandMessage command) {
        try {
            if (command.type() != CheckoutCommandType.PAYMENT_CHARGE) {
                checkoutReplySender.send(
                        command,
                        CheckoutReplyMessage.failure(
                                command.correlationId(),
                                "UNSUPPORTED_COMMAND",
                                "Unsupported payment command " + command.type()
                        )
                );
                return;
            }
            CheckoutPayloads.PaymentChargePayload payload = objectMapper.convertValue(
                    command.payload(),
                    CheckoutPayloads.PaymentChargePayload.class
            );
            Payment payment = createPaymentPort.execute(new CreatePaymentCommand(
                    payload.reference(),
                    payload.amount(),
                    payload.currency(),
                    payload.paymentMethodType(),
                    payload.paymentMethodToken(),
                    payload.idempotencyKey()
            ));
            checkoutReplySender.send(
                    command,
                    CheckoutReplyMessage.ok(
                            command.correlationId(),
                            new CheckoutPayloads.PaymentChargeReplyData(
                                    payment.transactionId(),
                                    payment.status().name()
                            )
                    )
            );
        } catch (Exception e) {
            log.warn("Payment checkout command failed: {}", e.getMessage());
            checkoutReplySender.send(
                    command,
                    CheckoutReplyMessage.failure(
                            command.correlationId(),
                            "PAYMENT_ERROR",
                            e.getMessage() != null ? e.getMessage() : "Payment command failed"
                    )
            );
        }
    }
}