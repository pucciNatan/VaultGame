package com.vaultgame.fakepaymentgateway.application.port.in;

import com.vaultgame.fakepaymentgateway.domain.model.Payment;

public interface GetPaymentPort {

    Payment execute(String transactionId);
}
