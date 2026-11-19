package com.vaultgame.inventory.adapter.in.messaging.checkout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaultgame.inventory.application.port.in.ReserveStockPort;
import com.vaultgame.inventory.domain.model.ReservationLine;
import com.vaultgame.inventory.domain.model.StockReservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryCheckoutCommandListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryCheckoutCommandListener.class);

    private final ReserveStockPort reserveStockPort;
    private final CheckoutReplySender checkoutReplySender;
    private final ObjectMapper objectMapper;

    public InventoryCheckoutCommandListener(
            ReserveStockPort reserveStockPort,
            CheckoutReplySender checkoutReplySender,
            ObjectMapper objectMapper
    ) {
        this.reserveStockPort = reserveStockPort;
        this.checkoutReplySender = checkoutReplySender;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = VaultGameCheckoutTopics.COMMANDS_INVENTORY,
            containerFactory = "checkoutCommandKafkaListenerContainerFactory"
    )
    public void onCommand(CheckoutCommandMessage command) {
        try {
            CheckoutReplyMessage reply = switch (command.type()) {
                case INVENTORY_RESERVE -> handleReserve(command);
                case INVENTORY_COMMIT -> handleCommit(command);
                case INVENTORY_RELEASE -> handleRelease(command);
                default -> CheckoutReplyMessage.failure(
                        command.correlationId(),
                        "UNSUPPORTED_COMMAND",
                        "Unsupported inventory command " + command.type()
                );
            };
            checkoutReplySender.send(command, reply);
        } catch (Exception e) {
            log.warn("Inventory checkout command failed: {}", e.getMessage());
            checkoutReplySender.send(
                    command,
                    CheckoutReplyMessage.failure(
                            command.correlationId(),
                            errorCode(e),
                            e.getMessage() != null ? e.getMessage() : "Inventory command failed"
                    )
            );
        }
    }

    private CheckoutReplyMessage handleReserve(CheckoutCommandMessage command) {
        CheckoutPayloads.InventoryReservePayload payload = objectMapper.convertValue(
                command.payload(),
                CheckoutPayloads.InventoryReservePayload.class
        );
        List<ReservationLine> lines = payload.lines().stream()
                .map(line -> new ReservationLine(line.productId(), line.quantity()))
                .toList();
        StockReservation reservation = reserveStockPort.reserve(payload.reference(), lines);
        return CheckoutReplyMessage.ok(
                command.correlationId(),
                new CheckoutPayloads.InventoryReserveReplyData(reservation.id())
        );
    }

    private CheckoutReplyMessage handleCommit(CheckoutCommandMessage command) {
        CheckoutPayloads.InventoryReservationIdPayload payload = objectMapper.convertValue(
                command.payload(),
                CheckoutPayloads.InventoryReservationIdPayload.class
        );
        reserveStockPort.commit(payload.reservationId());
        return CheckoutReplyMessage.ok(command.correlationId(), null);
    }

    private CheckoutReplyMessage handleRelease(CheckoutCommandMessage command) {
        CheckoutPayloads.InventoryReservationIdPayload payload = objectMapper.convertValue(
                command.payload(),
                CheckoutPayloads.InventoryReservationIdPayload.class
        );
        reserveStockPort.release(payload.reservationId());
        return CheckoutReplyMessage.ok(command.correlationId(), null);
    }

    private static String errorCode(Exception e) {
        return e.getClass().getSimpleName().replace("Exception", "").toUpperCase();
    }
}