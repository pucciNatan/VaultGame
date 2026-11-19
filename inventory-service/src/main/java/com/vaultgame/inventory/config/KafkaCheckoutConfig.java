package com.vaultgame.inventory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaultgame.inventory.adapter.in.messaging.checkout.CheckoutCommandMessage;
import com.vaultgame.inventory.adapter.in.messaging.checkout.CheckoutReplyMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaCheckoutConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }

    @Bean
    ConsumerFactory<String, CheckoutCommandMessage> checkoutCommandConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "inventory-service-checkout");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.vaultgame.inventory.adapter.in.messaging.checkout");
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(CheckoutCommandMessage.class, false)
        );
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, CheckoutCommandMessage> checkoutCommandKafkaListenerContainerFactory(
            ConsumerFactory<String, CheckoutCommandMessage> checkoutCommandConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, CheckoutCommandMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(checkoutCommandConsumerFactory);
        return factory;
    }

    @Bean
    ProducerFactory<String, CheckoutReplyMessage> checkoutReplyProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(
                props,
                new StringSerializer(),
                new JsonSerializer<>()
        );
    }

    @Bean
    KafkaTemplate<String, CheckoutReplyMessage> checkoutReplyKafkaTemplate(
            ProducerFactory<String, CheckoutReplyMessage> checkoutReplyProducerFactory
    ) {
        return new KafkaTemplate<>(checkoutReplyProducerFactory);
    }
}