package com.vaultgame.email.adapter.in.messaging;

import com.vaultgame.email.application.port.in.SendTemplatedEmailPort;
import com.vaultgame.email.config.EmailServiceApplication;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = EmailServiceApplication.class)
@Import(VaultGameEmailKafkaListenersEmbeddedKafkaTest.KafkaTestProducerConfig.class)
@EmbeddedKafka(
        partitions = 1,
        topics = VaultGameKafkaTopics.CUSTOMER_REGISTERED
)
@TestPropertySource(properties = {
        "spring.mail.host=localhost",
        "spring.mail.port=1025"
})
class VaultGameEmailKafkaListenersEmbeddedKafkaTest {

    @MockitoBean
    private SendTemplatedEmailPort sendTemplatedEmailPort;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaTestProducer kafkaTestProducer;

    @Autowired
    private VaultGameEmailKafkaListeners kafkaListeners;

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> System.getProperty("spring.embedded.kafka.brokers"));
    }

    @Test
    void publishesCustomerRegisteredEventConsumableAsJson() {
        UUID customerId = UUID.randomUUID();
        var message = new CustomerRegisteredMessage(
                UUID.randomUUID(),
                Instant.now(),
                new CustomerRegisteredPayload(customerId, "kafka@example.com", "Kafka User")
        );

        kafkaTestProducer.customerRegistered().send(
                VaultGameKafkaTopics.CUSTOMER_REGISTERED,
                customerId.toString(),
                message
        );
        kafkaTestProducer.customerRegistered().flush();

        try (Consumer<String, CustomerRegisteredMessage> consumer = testConsumer()) {
            consumer.subscribe(java.util.List.of(VaultGameKafkaTopics.CUSTOMER_REGISTERED));
            ConsumerRecord<String, CustomerRegisteredMessage> record = KafkaTestUtils.getSingleRecord(
                    consumer,
                    VaultGameKafkaTopics.CUSTOMER_REGISTERED,
                    Duration.ofSeconds(10)
            );
            kafkaListeners.onCustomerRegistered(record.value());
        }

        verify(sendTemplatedEmailPort, timeout(1000)).send(any());
    }

    private Consumer<String, CustomerRegisteredMessage> testConsumer() {
        Map<String, Object> props = new HashMap<>(KafkaTestUtils.consumerProps(
                "email-service-embedded-test",
                "false",
                embeddedKafkaBroker
        ));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.vaultgame.*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, CustomerRegisteredMessage.class);
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(CustomerRegisteredMessage.class, false)
        ).createConsumer();
    }

    @Configuration
    static class KafkaTestProducerConfig {

        @Bean
        KafkaTestProducer kafkaTestProducer(EmbeddedKafkaBroker embeddedKafkaBroker) {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString());
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
            var customerTemplate = new KafkaTemplate<>(
                    new DefaultKafkaProducerFactory<>(props, new StringSerializer(), new JsonSerializer<CustomerRegisteredMessage>())
            );
            return new KafkaTestProducer(customerTemplate);
        }
    }

    record KafkaTestProducer(KafkaTemplate<String, CustomerRegisteredMessage> customerRegistered) {
    }
}
