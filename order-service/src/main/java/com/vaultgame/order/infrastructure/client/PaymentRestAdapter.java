package com.vaultgame.order.infrastructure.client;

import com.vaultgame.order.domain.exception.DownstreamServiceException;
import com.vaultgame.order.domain.port.PaymentPort;
import com.vaultgame.order.domain.port.model.PaymentRequest;
import com.vaultgame.order.domain.port.model.PaymentResult;
import com.vaultgame.order.infrastructure.config.ServiceUrlsProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PaymentRestAdapter implements PaymentPort {

    private final RestClient restClient;

    public PaymentRestAdapter(RestClient.Builder restClientBuilder, ServiceUrlsProperties properties) {
        this.restClient = restClientBuilder.baseUrl(properties.payment().baseUrl()).build();
    }

    @Override
    public PaymentResult createPayment(PaymentRequest request, String idempotencyKey) {
        CreatePaymentRequest body = new CreatePaymentRequest(
                request.orderId().toString(),
                request.amount(),
                request.currency(),
                new PaymentMethodRequest(request.paymentMethodType(), request.paymentMethodToken())
        );
        PaymentResponse response = restClient.post()
                .uri("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> {
                    if (idempotencyKey != null && !idempotencyKey.isBlank()) {
                        headers.set("Idempotency-Key", idempotencyKey);
                    }
                })
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new DownstreamServiceException("payment", "Payment failed: " + res.getStatusCode());
                })
                .body(PaymentResponse.class);
        if (response == null) {
            throw new DownstreamServiceException("payment", "Empty payment response");
        }
        return new PaymentResult(response.transactionId(), mapStatus(response.status()));
    }

    private static PaymentResult.PaymentStatus mapStatus(String status) {
        return switch (status) {
            case "APPROVED" -> PaymentResult.PaymentStatus.APPROVED;
            case "PENDING" -> PaymentResult.PaymentStatus.PENDING;
            default -> PaymentResult.PaymentStatus.DECLINED;
        };
    }

    private record CreatePaymentRequest(
            String reference,
            java.math.BigDecimal amount,
            String currency,
            PaymentMethodRequest paymentMethod
    ) {
    }

    private record PaymentMethodRequest(String type, String token) {
    }

    private record PaymentResponse(String transactionId, String status) {
    }
}
