package com.vaultgame.customer.adapter.out.messaging;

import com.vaultgame.customer.application.event.CustomerRegisteredApplicationEvent;
import com.vaultgame.customer.application.port.out.CustomerEventPublisherPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CustomerRegisteredKafkaListener {

    private final CustomerEventPublisherPort customerEventPublisherPort;

    public CustomerRegisteredKafkaListener(CustomerEventPublisherPort customerEventPublisherPort) {
        this.customerEventPublisherPort = customerEventPublisherPort;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCustomerRegistered(CustomerRegisteredApplicationEvent event) {
        customerEventPublisherPort.publishRegistered(event.customerId(), event.email(), event.fullName());
    }
}
