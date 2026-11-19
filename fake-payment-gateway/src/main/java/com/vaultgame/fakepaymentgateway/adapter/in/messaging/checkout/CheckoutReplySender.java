package com.vaultgame.fakepaymentgateway.adapter.in.messaging.checkout;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class CheckoutReplySender {

    private final KafkaTemplate<String, CheckoutReplyMessage> kafkaTemplate;

    public CheckoutReplySender(KafkaTemplate<String, CheckoutReplyMessage> checkoutReplyKafkaTemplate) {
        this.kafkaTemplate = checkoutReplyKafkaTemplate;
    }

    public void send(CheckoutCommandMessage command, CheckoutReplyMessage reply) {
        String topic = command.replyTopic() != null ? command.replyTopic() : VaultGameCheckoutTopics.REPLIES;
        ProducerRecord<String, CheckoutReplyMessage> record =
                new ProducerRecord<>(topic, command.correlationId().toString(), reply);
        record.headers().add(new RecordHeader(
                KafkaHeaders.CORRELATION_ID,
                command.correlationId().toString().getBytes(StandardCharsets.UTF_8)
        ));
        kafkaTemplate.send(record);
    }
}