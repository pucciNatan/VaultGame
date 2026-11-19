package com.vaultgame.order.config;

import com.vaultgame.order.adapter.out.messaging.checkout.CheckoutCommandMessage;
import com.vaultgame.order.adapter.out.messaging.checkout.CheckoutReplyMessage;
import com.vaultgame.order.adapter.out.messaging.checkout.VaultGameCheckoutTopics;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(CheckoutKafkaProperties.class)
public class KafkaCheckoutConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    ProducerFactory<String, CheckoutCommandMessage> checkoutCommandProducerFactory() {
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
    ConsumerFactory<String, CheckoutReplyMessage> checkoutReplyConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-service-checkout-replies");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.vaultgame.order.adapter.out.messaging.checkout");
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(CheckoutReplyMessage.class, false)
        );
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, CheckoutReplyMessage> checkoutReplyContainerFactory(
            ConsumerFactory<String, CheckoutReplyMessage> checkoutReplyConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, CheckoutReplyMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(checkoutReplyConsumerFactory);
        return factory;
    }

    @Bean
    ConcurrentMessageListenerContainer<String, CheckoutReplyMessage> checkoutReplyContainer(
            ConcurrentKafkaListenerContainerFactory<String, CheckoutReplyMessage> checkoutReplyContainerFactory
    ) {
        ConcurrentMessageListenerContainer<String, CheckoutReplyMessage> container =
                checkoutReplyContainerFactory.createContainer(VaultGameCheckoutTopics.REPLIES);
        return container;
    }

    @Bean
    ReplyingKafkaTemplate<String, CheckoutCommandMessage, CheckoutReplyMessage> checkoutReplyingKafkaTemplate(
            ProducerFactory<String, CheckoutCommandMessage> checkoutCommandProducerFactory,
            ConcurrentMessageListenerContainer<String, CheckoutReplyMessage> checkoutReplyContainer,
            CheckoutKafkaProperties checkoutKafkaProperties
    ) {
        ReplyingKafkaTemplate<String, CheckoutCommandMessage, CheckoutReplyMessage> template =
                new ReplyingKafkaTemplate<>(checkoutCommandProducerFactory, checkoutReplyContainer);
        template.setDefaultReplyTimeout(checkoutKafkaProperties.replyTimeout());
        template.setSharedReplyTopic(true);
        return template;
    }
}