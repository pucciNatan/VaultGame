package com.vaultgame.api.adapter.in.messaging.checkout;

import tools.jackson.databind.ObjectMapper;
import com.vaultgame.api.application.port.in.GetProductByIdPort;
import com.vaultgame.api.domain.exception.ProductNotFoundException;
import com.vaultgame.api.domain.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CatalogCheckoutCommandListener {

    private static final Logger log = LoggerFactory.getLogger(CatalogCheckoutCommandListener.class);

    private final GetProductByIdPort getProductByIdPort;
    private final CheckoutReplySender checkoutReplySender;
    private final ObjectMapper objectMapper;

    public CatalogCheckoutCommandListener(
            GetProductByIdPort getProductByIdPort,
            CheckoutReplySender checkoutReplySender,
            @Qualifier("checkoutObjectMapper") ObjectMapper objectMapper
    ) {
        this.getProductByIdPort = getProductByIdPort;
        this.checkoutReplySender = checkoutReplySender;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = VaultGameCheckoutTopics.COMMANDS_CATALOG,
            containerFactory = "checkoutCommandKafkaListenerContainerFactory"
    )
    public void onCommand(CheckoutCommandMessage command) {
        try {
            if (command.type() != CheckoutCommandType.CATALOG_RESOLVE) {
                checkoutReplySender.send(
                        command,
                        CheckoutReplyMessage.failure(
                                command.correlationId(),
                                "UNSUPPORTED_COMMAND",
                                "Unsupported catalog command " + command.type()
                        )
                );
                return;
            }
            CheckoutPayloads.CatalogResolvePayload payload = objectMapper.convertValue(
                    command.payload(),
                    CheckoutPayloads.CatalogResolvePayload.class
            );
            List<CheckoutPayloads.CatalogProductLine> products = new ArrayList<>();
            for (String productId : payload.productIds()) {
                Product product = getProductByIdPort.execute(productId);
                boolean active = product.active() == null || product.active();
                products.add(new CheckoutPayloads.CatalogProductLine(
                        product.id(),
                        product.name(),
                        product.price(),
                        active
                ));
            }
            checkoutReplySender.send(
                    command,
                    CheckoutReplyMessage.ok(
                            command.correlationId(),
                            new CheckoutPayloads.CatalogResolveReplyData(products)
                    )
            );
        } catch (ProductNotFoundException e) {
            checkoutReplySender.send(
                    command,
                    CheckoutReplyMessage.failure(
                            command.correlationId(),
                            "CATALOG_PRODUCT_NOT_FOUND",
                            e.getMessage()
                    )
            );
        } catch (Exception e) {
            log.warn("Catalog checkout command failed: {}", e.getMessage());
            checkoutReplySender.send(
                    command,
                    CheckoutReplyMessage.failure(
                            command.correlationId(),
                            "CATALOG_ERROR",
                            e.getMessage() != null ? e.getMessage() : "Catalog command failed"
                    )
            );
        }
    }
}