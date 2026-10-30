package com.vaultgame.fakepaymentgateway.application.port.in;

import com.vaultgame.fakepaymentgateway.application.usecase.CreatePaymentCommand;
import com.vaultgame.fakepaymentgateway.domain.model.Payment;

public interface CreatePaymentPort {

    Payment execute(CreatePaymentCommand command);
}
