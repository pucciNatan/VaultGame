package com.vaultgame.email.config;

import com.vaultgame.email.adapter.in.messaging.CustomerRegisteredMessage;
import com.vaultgame.email.adapter.in.messaging.InMemoryProcessedEventStore;
import com.vaultgame.email.adapter.in.messaging.OrderPaidMessage;
import com.vaultgame.email.adapter.in.messaging.ProcessedEventStore;
import com.vaultgame.email.adapter.in.messaging.VaultGameEmailKafkaListeners;
import com.vaultgame.email.application.port.in.SendTemplatedEmailPort;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, CustomerRegisteredMessage> customerRegisteredKafkaListenerContainerFactory() {
        return listenerFactory(customerRegisteredConsumerFactory());
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, OrderPaidMessage> orderPaidKafkaListenerContainerFactory() {
        return listenerFactory(orderPaidConsumerFactory());
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> listenerFactory(ConsumerFactory<String, T> factory) {
        ConcurrentKafkaListenerContainerFactory<String, T> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(factory);
        return containerFactory;
    }

    @Bean
    ConsumerFactory<String, CustomerRegisteredMessage> customerRegisteredConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerProps(CustomerRegisteredMessage.class),
                new StringDeserializer(),
                new JsonDeserializer<>(CustomerRegisteredMessage.class, false)
        );
    }

    @Bean
    ConsumerFactory<String, OrderPaidMessage> orderPaidConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerProps(OrderPaidMessage.class),
                new StringDeserializer(),
                new JsonDeserializer<>(OrderPaidMessage.class, false)
        );
    }

    private Map<String, Object> consumerProps(Class<?> valueType) {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "email-service");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.vaultgame.*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueType);
        return props;
    }

    @Bean
    ProcessedEventStore processedEventStore() {
        return new InMemoryProcessedEventStore();
    }

    @Bean
    VaultGameEmailKafkaListeners vaultGameEmailKafkaListeners(
            SendTemplatedEmailPort sendTemplatedEmailPort,
            ProcessedEventStore processedEventStore
    ) {
        return new VaultGameEmailKafkaListeners(sendTemplatedEmailPort, processedEventStore);
    }
}
