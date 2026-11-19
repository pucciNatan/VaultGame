package com.vaultgame.customer.adapter.out.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaCustomerEventPublisherTest {

    @Mock
    private KafkaTemplate<String, CustomerRegisteredMessage> kafkaTemplate;

    @InjectMocks
    private KafkaCustomerEventPublisher publisher;

    @Test
    void publishRegisteredSendsToTopicWithCustomerIdKey() {
        UUID customerId = UUID.randomUUID();
        when(kafkaTemplate.send(
                eq(VaultGameKafkaTopics.CUSTOMER_REGISTERED),
                eq(customerId.toString()),
                org.mockito.ArgumentMatchers.any(CustomerRegisteredMessage.class)
        )).thenReturn(CompletableFuture.completedFuture(null));

        publisher.publishRegistered(customerId, "user@example.com", "Ada");

        ArgumentCaptor<CustomerRegisteredMessage> captor = ArgumentCaptor.forClass(CustomerRegisteredMessage.class);
        verify(kafkaTemplate).send(
                eq(VaultGameKafkaTopics.CUSTOMER_REGISTERED),
                eq(customerId.toString()),
                captor.capture()
        );
        CustomerRegisteredMessage message = captor.getValue();
        assertThat(message.payload().customerId()).isEqualTo(customerId);
        assertThat(message.payload().email()).isEqualTo("user@example.com");
        assertThat(message.payload().fullName()).isEqualTo("Ada");
        assertThat(message.eventId()).isNotNull();
        assertThat(message.occurredAt()).isNotNull();
    }
}
