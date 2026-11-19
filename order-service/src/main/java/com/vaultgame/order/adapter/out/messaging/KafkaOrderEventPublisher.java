package com.vaultgame.order.adapter.out.messaging;

import com.vaultgame.order.application.port.out.OrderEventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class KafkaOrderEventPublisher implements OrderEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaOrderEventPublisher.class);

    private final KafkaTemplate<String, OrderPaidMessage> kafkaTemplate;

    public KafkaOrderEventPublisher(KafkaTemplate<String, OrderPaidMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishPaid(
            UUID orderId,
            UUID customerId,
            String email,
            String fullName,
            BigDecimal total,
            String currency
    ) {
        OrderPaidMessage message = OrderPaidMessage.create(orderId, customerId, email, fullName, total, currency);
        try {
            kafkaTemplate.send(VaultGameKafkaTopics.ORDER_PAID, orderId.toString(), message)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error(
                                    "Failed to publish order.paid for orderId={} eventId={}",
                                    orderId,
                                    message.eventId(),
                                    ex
                            );
                        }
                    });
        } catch (Exception ex) {
            log.error(
                    "Failed to enqueue order.paid for orderId={} eventId={}",
                    orderId,
                    message.eventId(),
                    ex
            );
        }
    }
}
