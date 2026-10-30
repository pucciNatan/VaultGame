package com.vaultgame.order.application.port.out;

import com.vaultgame.order.application.port.out.model.PaymentRequest;
import com.vaultgame.order.application.port.out.model.PaymentResult;

public interface PaymentPort {

    PaymentResult createPayment(PaymentRequest request, String idempotencyKey);
}
