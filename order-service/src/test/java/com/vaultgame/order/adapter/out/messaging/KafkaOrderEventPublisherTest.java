package com.vaultgame.order.adapter.out.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaOrderEventPublisherTest {

    @Mock
    private KafkaTemplate<String, OrderPaidMessage> kafkaTemplate;

    @InjectMocks
    private KafkaOrderEventPublisher publisher;

    @Test
    void publishPaidSendsToTopicWithOrderIdKey() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        when(kafkaTemplate.send(
                eq(VaultGameKafkaTopics.ORDER_PAID),
                eq(orderId.toString()),
                org.mockito.ArgumentMatchers.any(OrderPaidMessage.class)
        )).thenReturn(CompletableFuture.completedFuture(null));

        publisher.publishPaid(
                orderId,
                customerId,
                "buyer@example.com",
                "Grace",
                new BigDecimal("42.00"),
                "BRL"
        );

        ArgumentCaptor<OrderPaidMessage> captor = ArgumentCaptor.forClass(OrderPaidMessage.class);
        verify(kafkaTemplate).send(eq(VaultGameKafkaTopics.ORDER_PAID), eq(orderId.toString()), captor.capture());
        OrderPaidMessage message = captor.getValue();
        assertThat(message.payload().orderId()).isEqualTo(orderId);
        assertThat(message.payload().customerId()).isEqualTo(customerId);
        assertThat(message.payload().email()).isEqualTo("buyer@example.com");
        assertThat(message.payload().total()).isEqualByComparingTo("42.00");
        assertThat(message.payload().currency()).isEqualTo("BRL");
    }
}
