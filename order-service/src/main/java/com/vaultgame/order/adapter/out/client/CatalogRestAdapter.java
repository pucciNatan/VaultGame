package com.vaultgame.order.adapter.out.client;

import com.vaultgame.order.domain.exception.CatalogProductNotFoundException;
import com.vaultgame.order.domain.exception.DownstreamServiceException;
import com.vaultgame.order.application.port.out.CatalogPort;
import com.vaultgame.order.application.port.out.model.CatalogProduct;
import com.vaultgame.order.config.ServiceUrlsProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CatalogRestAdapter implements CatalogPort {

    private final RestClient restClient;

    public CatalogRestAdapter(RestClient.Builder restClientBuilder, ServiceUrlsProperties properties) {
        this.restClient = restClientBuilder.baseUrl(properties.catalog().baseUrl()).build();
    }

    @Override
    public List<CatalogProduct> getProducts(Collection<String> productIds) {
        List<CatalogProduct> products = new ArrayList<>();
        for (String productId : productIds) {
            CatalogApiResponse response = restClient.get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        if (res.getStatusCode().value() == 404) {
                            throw new CatalogProductNotFoundException(productId);
                        }
                        throw new DownstreamServiceException("catalog", "Client error " + res.getStatusCode());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new DownstreamServiceException("catalog", "Server error " + res.getStatusCode());
                    })
                    .body(CatalogApiResponse.class);
            if (response == null) {
                throw new CatalogProductNotFoundException(productId);
            }
            products.add(new CatalogProduct(
                    response.id(),
                    response.name(),
                    response.price(),
                    Boolean.TRUE.equals(response.active())
            ));
        }
        return products;
    }

    private record CatalogApiResponse(
            String id,
            String name,
            BigDecimal price,
            Boolean active
    ) {
    }
}
