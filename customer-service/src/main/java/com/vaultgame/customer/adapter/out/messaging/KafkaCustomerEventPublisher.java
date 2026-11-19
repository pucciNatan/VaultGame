package com.vaultgame.customer.adapter.out.messaging;

import com.vaultgame.customer.application.port.out.CustomerEventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaCustomerEventPublisher implements CustomerEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaCustomerEventPublisher.class);

    private final KafkaTemplate<String, CustomerRegisteredMessage> kafkaTemplate;

    public KafkaCustomerEventPublisher(KafkaTemplate<String, CustomerRegisteredMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishRegistered(UUID customerId, String email, String fullName) {
        CustomerRegisteredMessage message = CustomerRegisteredMessage.create(customerId, email, fullName);
        try {
            kafkaTemplate.send(VaultGameKafkaTopics.CUSTOMER_REGISTERED, customerId.toString(), message)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error(
                                    "Failed to publish customer.registered for customerId={} eventId={}",
                                    customerId,
                                    message.eventId(),
                                    ex
                            );
                        }
                    });
        } catch (Exception ex) {
            log.error(
                    "Failed to enqueue customer.registered for customerId={} eventId={}",
                    customerId,
                    message.eventId(),
                    ex
            );
        }
    }
}
