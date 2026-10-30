package com.vaultgame.order.adapter.out.client;

import com.vaultgame.order.domain.exception.DownstreamServiceException;
import com.vaultgame.order.application.port.out.EmailPort;
import com.vaultgame.order.config.ServiceUrlsProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Component
public class EmailRestAdapter implements EmailPort {

    private final RestClient restClient;

    public EmailRestAdapter(RestClient.Builder restClientBuilder, ServiceUrlsProperties properties) {
        this.restClient = restClientBuilder.baseUrl(properties.email().baseUrl()).build();
    }

    @Override
    public void sendOrderConfirmed(String to, UUID orderId, String fullName) {
        SendEmailRequest request = new SendEmailRequest(
                "ORDER_CONFIRMED",
                to,
                null,
                Map.of(
                        "orderId", orderId.toString(),
                        "fullName", fullName
                )
        );
        restClient.post()
                .uri("/emails/send")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new DownstreamServiceException("email", "Send failed: " + res.getStatusCode());
                })
                .toBodilessEntity();
    }

    private record SendEmailRequest(
            String template,
            String to,
            String subject,
            Map<String, Object> variables
    ) {
    }
}
