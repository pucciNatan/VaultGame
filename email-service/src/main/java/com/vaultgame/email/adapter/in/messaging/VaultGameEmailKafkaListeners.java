package com.vaultgame.email.adapter.in.messaging;

import com.vaultgame.email.application.port.in.SendTemplatedEmailPort;
import com.vaultgame.email.application.usecase.SendEmailCommand;
import com.vaultgame.email.domain.model.EmailTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;

public class VaultGameEmailKafkaListeners {

    private static final Logger log = LoggerFactory.getLogger(VaultGameEmailKafkaListeners.class);

    private final SendTemplatedEmailPort sendTemplatedEmailPort;
    private final ProcessedEventStore processedEventStore;

    public VaultGameEmailKafkaListeners(
            SendTemplatedEmailPort sendTemplatedEmailPort,
            ProcessedEventStore processedEventStore
    ) {
        this.sendTemplatedEmailPort = sendTemplatedEmailPort;
        this.processedEventStore = processedEventStore;
    }

    @KafkaListener(
            topics = VaultGameKafkaTopics.CUSTOMER_REGISTERED,
            containerFactory = "customerRegisteredKafkaListenerContainerFactory"
    )
    public void onCustomerRegistered(CustomerRegisteredMessage message) {
        if (!processedEventStore.markProcessedIfNew(message.eventId())) {
            log.debug("Skipping duplicate customer.registered event {}", message.eventId());
            return;
        }
        var payload = message.payload();
        sendTemplatedEmailPort.send(new SendEmailCommand(
                EmailTemplate.WELCOME,
                payload.email(),
                EmailTemplate.WELCOME.defaultSubject(),
                Map.of(
                        "fullName", payload.fullName(),
                        "email", payload.email()
                )
        ));
    }

    @KafkaListener(
            topics = VaultGameKafkaTopics.ORDER_PAID,
            containerFactory = "orderPaidKafkaListenerContainerFactory"
    )
    public void onOrderPaid(OrderPaidMessage message) {
        if (!processedEventStore.markProcessedIfNew(message.eventId())) {
            log.debug("Skipping duplicate order.paid event {}", message.eventId());
            return;
        }
        var payload = message.payload();
        sendTemplatedEmailPort.send(new SendEmailCommand(
                EmailTemplate.ORDER_CONFIRMED,
                payload.email(),
                EmailTemplate.ORDER_CONFIRMED.defaultSubject(),
                Map.of(
                        "orderId", payload.orderId().toString(),
                        "fullName", payload.fullName()
                )
        ));
    }
}
