package com.vaultgame.email.adapter.in.messaging;

import com.vaultgame.email.application.port.in.SendTemplatedEmailPort;
import com.vaultgame.email.application.usecase.SendEmailCommand;
import com.vaultgame.email.domain.model.EmailTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class VaultGameEmailKafkaListenersUnitTest {

    @Mock
    private SendTemplatedEmailPort sendTemplatedEmailPort;

    private InMemoryProcessedEventStore processedEventStore;
    private VaultGameEmailKafkaListeners listeners;

    @BeforeEach
    void setUp() {
        processedEventStore = new InMemoryProcessedEventStore();
        listeners = new VaultGameEmailKafkaListeners(sendTemplatedEmailPort, processedEventStore);
    }

    @Test
    void onCustomerRegisteredSendsWelcomeEmail() {
        UUID eventId = UUID.randomUUID();
        var message = new CustomerRegisteredMessage(
                eventId,
                Instant.now(),
                new CustomerRegisteredPayload(UUID.randomUUID(), "user@example.com", "Ada Lovelace")
        );

        listeners.onCustomerRegistered(message);

        ArgumentCaptor<SendEmailCommand> captor = ArgumentCaptor.forClass(SendEmailCommand.class);
        verify(sendTemplatedEmailPort).send(captor.capture());
        SendEmailCommand command = captor.getValue();
        assertThat(command.template()).isEqualTo(EmailTemplate.WELCOME);
        assertThat(command.to()).isEqualTo("user@example.com");
        assertThat(command.variables().get("fullName")).isEqualTo("Ada Lovelace");
    }

    @Test
    void onOrderPaidSendsOrderConfirmedEmail() {
        UUID orderId = UUID.randomUUID();
        var message = new OrderPaidMessage(
                UUID.randomUUID(),
                Instant.now(),
                new OrderPaidPayload(
                        orderId,
                        UUID.randomUUID(),
                        "buyer@example.com",
                        "Grace Hopper",
                        new BigDecimal("10.00"),
                        "BRL"
                )
        );

        listeners.onOrderPaid(message);

        ArgumentCaptor<SendEmailCommand> captor = ArgumentCaptor.forClass(SendEmailCommand.class);
        verify(sendTemplatedEmailPort).send(captor.capture());
        assertThat(captor.getValue().template()).isEqualTo(EmailTemplate.ORDER_CONFIRMED);
        assertThat(captor.getValue().variables().get("orderId")).isEqualTo(orderId.toString());
    }

    @Test
    void skipsDuplicateEventId() {
        UUID eventId = UUID.randomUUID();
        var message = new CustomerRegisteredMessage(
                eventId,
                Instant.now(),
                new CustomerRegisteredPayload(UUID.randomUUID(), "dup@example.com", "Dup")
        );

        listeners.onCustomerRegistered(message);
        listeners.onCustomerRegistered(message);

        verify(sendTemplatedEmailPort, times(1)).send(org.mockito.ArgumentMatchers.any());
        verifyNoMoreInteractions(sendTemplatedEmailPort);
    }
}
