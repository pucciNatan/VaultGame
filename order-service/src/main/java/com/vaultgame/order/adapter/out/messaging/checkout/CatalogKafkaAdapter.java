package com.vaultgame.order.adapter.out.messaging.checkout;

import com.vaultgame.order.application.port.out.CatalogPort;
import com.vaultgame.order.application.port.out.model.CatalogProduct;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class CatalogKafkaAdapter implements CatalogPort {

    private final KafkaCheckoutRequester kafkaCheckoutRequester;

    public CatalogKafkaAdapter(KafkaCheckoutRequester kafkaCheckoutRequester) {
        this.kafkaCheckoutRequester = kafkaCheckoutRequester;
    }

    @Override
    public List<CatalogProduct> getProducts(Collection<String> productIds) {
        CheckoutPayloads.CatalogResolveReplyData reply = kafkaCheckoutRequester.execute(
                VaultGameCheckoutTopics.COMMANDS_CATALOG,
                CheckoutCommandType.CATALOG_RESOLVE,
                new CheckoutPayloads.CatalogResolvePayload(List.copyOf(productIds)),
                CheckoutPayloads.CatalogResolveReplyData.class
        );
        return reply.products().stream()
                .map(line -> new CatalogProduct(line.id(), line.name(), line.price(), line.active()))
                .toList();
    }
}