package com.vaultgame.order.adapter.out.messaging.checkout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaultgame.order.config.CheckoutKafkaProperties;
import com.vaultgame.order.domain.exception.CatalogProductNotFoundException;
import com.vaultgame.order.domain.exception.DownstreamServiceException;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class KafkaCheckoutRequester {

    private final ReplyingKafkaTemplate<String, CheckoutCommandMessage, CheckoutReplyMessage> replyingKafkaTemplate;
    private final CheckoutKafkaProperties properties;
    private final ObjectMapper objectMapper;

    public KafkaCheckoutRequester(
            ReplyingKafkaTemplate<String, CheckoutCommandMessage, CheckoutReplyMessage> checkoutReplyingKafkaTemplate,
            CheckoutKafkaProperties properties,
            ObjectMapper objectMapper
    ) {
        this.replyingKafkaTemplate = checkoutReplyingKafkaTemplate;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public <R> R execute(
            String commandTopic,
            CheckoutCommandType type,
            Object payload,
            Class<R> replyDataType
    ) {
        UUID correlationId = UUID.randomUUID();
        CheckoutCommandMessage command = new CheckoutCommandMessage(
                correlationId,
                VaultGameCheckoutTopics.REPLIES,
                type,
                payload
        );
        ProducerRecord<String, CheckoutCommandMessage> record =
                new ProducerRecord<>(commandTopic, correlationId.toString(), command);

        try {
            RequestReplyFuture<String, CheckoutCommandMessage, CheckoutReplyMessage> future =
                    replyingKafkaTemplate.sendAndReceive(record);
            CheckoutReplyMessage reply = future.get(
                    properties.replyTimeout().toMillis(),
                    TimeUnit.MILLISECONDS
            ).value();
            if (reply == null) {
                throw new DownstreamServiceException(topicService(commandTopic), "Empty checkout reply");
            }
            if (!reply.success()) {
                throw mapFailure(commandTopic, reply);
            }
            if (reply.data() == null) {
                return null;
            }
            return objectMapper.convertValue(reply.data(), replyDataType);
        } catch (DownstreamServiceException e) {
            throw e;
        } catch (TimeoutException e) {
            throw new DownstreamServiceException(topicService(commandTopic), "Checkout command timed out");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DownstreamServiceException(topicService(commandTopic), "Checkout command interrupted");
        } catch (Exception e) {
            throw new DownstreamServiceException(topicService(commandTopic), e.getMessage());
        }
    }

    public void executeVoid(String commandTopic, CheckoutCommandType type, Object payload) {
        execute(commandTopic, type, payload, Object.class);
    }

    private RuntimeException mapFailure(String commandTopic, CheckoutReplyMessage reply) {
        String service = topicService(commandTopic);
        String message = reply.errorCode() != null ? reply.errorCode() + ": " + reply.errorMessage() : reply.errorMessage();
        if ("CATALOG_PRODUCT_NOT_FOUND".equals(reply.errorCode())) {
            return new CatalogProductNotFoundException(reply.errorMessage());
        }
        return new DownstreamServiceException(service, message);
    }

    private static String topicService(String topic) {
        if (topic.contains("catalog")) {
            return "catalog";
        }
        if (topic.contains("inventory")) {
            return "inventory";
        }
        if (topic.contains("payment")) {
            return "payment";
        }
        return "checkout";
    }
}