package com.vaultgame.order.adapter.out.messaging.checkout;

import com.vaultgame.order.application.port.out.InventoryPort;
import com.vaultgame.order.application.port.out.model.ReservationRequestLine;
import com.vaultgame.order.application.port.out.model.StockReservationResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class InventoryKafkaAdapter implements InventoryPort {

    private final KafkaCheckoutRequester kafkaCheckoutRequester;

    public InventoryKafkaAdapter(KafkaCheckoutRequester kafkaCheckoutRequester) {
        this.kafkaCheckoutRequester = kafkaCheckoutRequester;
    }

    @Override
    public StockReservationResult reserve(String reference, List<ReservationRequestLine> lines) {
        List<CheckoutPayloads.InventoryLinePayload> payloadLines = lines.stream()
                .map(line -> new CheckoutPayloads.InventoryLinePayload(line.productId(), line.quantity()))
                .toList();
        CheckoutPayloads.InventoryReserveReplyData reply = kafkaCheckoutRequester.execute(
                VaultGameCheckoutTopics.COMMANDS_INVENTORY,
                CheckoutCommandType.INVENTORY_RESERVE,
                new CheckoutPayloads.InventoryReservePayload(reference, payloadLines),
                CheckoutPayloads.InventoryReserveReplyData.class
        );
        return new StockReservationResult(reply.reservationId());
    }

    @Override
    public void commit(UUID reservationId) {
        kafkaCheckoutRequester.executeVoid(
                VaultGameCheckoutTopics.COMMANDS_INVENTORY,
                CheckoutCommandType.INVENTORY_COMMIT,
                new CheckoutPayloads.InventoryReservationIdPayload(reservationId)
        );
    }

    @Override
    public void release(UUID reservationId) {
        kafkaCheckoutRequester.executeVoid(
                VaultGameCheckoutTopics.COMMANDS_INVENTORY,
                CheckoutCommandType.INVENTORY_RELEASE,
                new CheckoutPayloads.InventoryReservationIdPayload(reservationId)
        );
    }
}