package com.vaultgame.order.infrastructure.client;

import com.vaultgame.order.domain.exception.DownstreamServiceException;
import com.vaultgame.order.domain.port.InventoryPort;
import com.vaultgame.order.domain.port.model.ReservationRequestLine;
import com.vaultgame.order.domain.port.model.StockReservationResult;
import com.vaultgame.order.infrastructure.config.ServiceUrlsProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Component
public class InventoryRestAdapter implements InventoryPort {

    private final RestClient restClient;

    public InventoryRestAdapter(RestClient.Builder restClientBuilder, ServiceUrlsProperties properties) {
        this.restClient = restClientBuilder.baseUrl(properties.inventory().baseUrl()).build();
    }

    @Override
    public StockReservationResult reserve(String reference, List<ReservationRequestLine> lines) {
        CreateReservationRequest request = new CreateReservationRequest(
                reference,
                lines.stream()
                        .map(line -> new ReservationLineRequest(line.productId(), line.quantity()))
                        .toList()
        );
        ReservationResponse response = restClient.post()
                .uri("/internal/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new DownstreamServiceException("inventory", "Reservation failed: " + res.getStatusCode());
                })
                .body(ReservationResponse.class);
        if (response == null || response.id() == null) {
            throw new DownstreamServiceException("inventory", "Empty reservation response");
        }
        return new StockReservationResult(UUID.fromString(response.id()));
    }

    @Override
    public void commit(UUID reservationId) {
        restClient.post()
                .uri("/internal/reservations/{id}/commit", reservationId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new DownstreamServiceException("inventory", "Commit failed: " + res.getStatusCode());
                })
                .toBodilessEntity();
    }

    @Override
    public void release(UUID reservationId) {
        restClient.post()
                .uri("/internal/reservations/{id}/release", reservationId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new DownstreamServiceException("inventory", "Release failed: " + res.getStatusCode());
                })
                .toBodilessEntity();
    }

    private record CreateReservationRequest(String reference, List<ReservationLineRequest> lines) {
    }

    private record ReservationLineRequest(String productId, int quantity) {
    }

    private record ReservationResponse(String id) {
    }
}
