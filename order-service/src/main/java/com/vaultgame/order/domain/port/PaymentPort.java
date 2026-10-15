package com.vaultgame.order.domain.port;

import com.vaultgame.order.domain.port.model.PaymentRequest;
import com.vaultgame.order.domain.port.model.PaymentResult;

public interface PaymentPort {

    PaymentResult createPayment(PaymentRequest request, String idempotencyKey);
}
