package com.vaultgame.fakepaymentgateway.application.usecase;

import com.vaultgame.fakepaymentgateway.application.port.in.GetPaymentPort;
import com.vaultgame.fakepaymentgateway.application.port.out.PaymentRepositoryPort;
import com.vaultgame.fakepaymentgateway.domain.exception.PaymentNotFoundException;
import com.vaultgame.fakepaymentgateway.domain.model.Payment;

public class GetPaymentUseCase implements GetPaymentPort {

    private final PaymentRepositoryPort paymentRepository;

    public GetPaymentUseCase(PaymentRepositoryPort paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment execute(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentNotFoundException(transactionId));
    }
}
