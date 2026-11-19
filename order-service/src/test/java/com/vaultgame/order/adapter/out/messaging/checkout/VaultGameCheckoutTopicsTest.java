package com.vaultgame.order.adapter.out.messaging.checkout;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VaultGameCheckoutTopicsTest {

    @Test
    void definesCheckoutSagaTopics() {
        assertThat(VaultGameCheckoutTopics.COMMANDS_CATALOG).isEqualTo("vaultgame.checkout.commands.catalog");
        assertThat(VaultGameCheckoutTopics.COMMANDS_INVENTORY).isEqualTo("vaultgame.checkout.commands.inventory");
        assertThat(VaultGameCheckoutTopics.COMMANDS_PAYMENT).isEqualTo("vaultgame.checkout.commands.payment");
        assertThat(VaultGameCheckoutTopics.REPLIES).isEqualTo("vaultgame.checkout.replies");
    }
}