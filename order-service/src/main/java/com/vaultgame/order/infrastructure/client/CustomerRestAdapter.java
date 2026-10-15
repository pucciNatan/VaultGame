package com.vaultgame.order.infrastructure.client;

import com.vaultgame.order.domain.exception.DownstreamServiceException;
import com.vaultgame.order.domain.port.CustomerPort;
import com.vaultgame.order.domain.port.model.CustomerProfile;
import com.vaultgame.order.infrastructure.config.ServiceUrlsProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class CustomerRestAdapter implements CustomerPort {

    private final RestClient restClient;

    public CustomerRestAdapter(RestClient.Builder restClientBuilder, ServiceUrlsProperties properties) {
        this.restClient = restClientBuilder.baseUrl(properties.customer().baseUrl()).build();
    }

    @Override
    public CustomerProfile getProfile(UUID customerId, String bearerToken) {
        CustomerResponse response = restClient.get()
                .uri("/customers/me")
                .headers(headers -> headers.setBearerAuth(stripBearerPrefix(bearerToken)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new DownstreamServiceException("customer", "Profile fetch failed: " + res.getStatusCode());
                })
                .body(CustomerResponse.class);
        if (response == null) {
            throw new DownstreamServiceException("customer", "Empty profile response");
        }
        return new CustomerProfile(response.email(), response.fullName());
    }

    private static String stripBearerPrefix(String bearerToken) {
        if (bearerToken == null) {
            return "";
        }
        if (bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return bearerToken;
    }

    private record CustomerResponse(String email, String fullName) {
    }
}
