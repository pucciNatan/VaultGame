package com.vaultgame.order.adapter.out.messaging.checkout;

import com.vaultgame.order.application.port.out.PaymentPort;
import com.vaultgame.order.application.port.out.model.PaymentRequest;
import com.vaultgame.order.application.port.out.model.PaymentResult;
import org.springframework.stereotype.Component;

@Component
public class PaymentKafkaAdapter implements PaymentPort {

    private final KafkaCheckoutRequester kafkaCheckoutRequester;

    public PaymentKafkaAdapter(KafkaCheckoutRequester kafkaCheckoutRequester) {
        this.kafkaCheckoutRequester = kafkaCheckoutRequester;
    }

    @Override
    public PaymentResult createPayment(PaymentRequest request, String idempotencyKey) {
        CheckoutPayloads.PaymentChargeReplyData reply = kafkaCheckoutRequester.execute(
                VaultGameCheckoutTopics.COMMANDS_PAYMENT,
                CheckoutCommandType.PAYMENT_CHARGE,
                new CheckoutPayloads.PaymentChargePayload(
                        request.orderId().toString(),
                        request.amount(),
                        request.currency(),
                        request.paymentMethodType(),
                        request.paymentMethodToken(),
                        idempotencyKey
                ),
                CheckoutPayloads.PaymentChargeReplyData.class
        );
        return new PaymentResult(reply.transactionId(), mapStatus(reply.status()));
    }

    private static PaymentResult.PaymentStatus mapStatus(String status) {
        return switch (status) {
            case "APPROVED" -> PaymentResult.PaymentStatus.APPROVED;
            case "PENDING" -> PaymentResult.PaymentStatus.PENDING;
            default -> PaymentResult.PaymentStatus.DECLINED;
        };
    }
}