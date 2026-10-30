package com.vaultgame.order.adapter.in.web.controllers;

import com.vaultgame.order.adapter.in.web.CartIdentityResolver;
import com.vaultgame.order.adapter.in.web.dto.OrderDtos;
import com.vaultgame.order.application.port.in.GetOrderPort;
import com.vaultgame.order.application.port.in.PlaceOrderPort;
import com.vaultgame.order.application.usecase.CheckoutCommand;
import com.vaultgame.order.domain.model.order.Order;
import com.vaultgame.order.domain.model.order.OrderStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PlaceOrderPort placeOrderPort;
    private final GetOrderPort getOrderPort;
    private final CartIdentityResolver cartIdentityResolver;

    public OrderController(
            PlaceOrderPort placeOrderPort,
            GetOrderPort getOrderPort,
            CartIdentityResolver cartIdentityResolver
    ) {
        this.placeOrderPort = placeOrderPort;
        this.getOrderPort = getOrderPort;
        this.cartIdentityResolver = cartIdentityResolver;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderDtos.OrderResponse> checkout(
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody OrderDtos.CheckoutRequest request
    ) {
        UUID customerId = cartIdentityResolver.requireCustomerId(jwt);
        String cartKey = cartIdentityResolver.resolveCartKey(null);
        Order order = placeOrderPort.checkout(new CheckoutCommand(
                customerId,
                cartKey,
                request.shippingAddressId(),
                request.paymentMethod().type(),
                request.paymentMethod().token(),
                idempotencyKey,
                authorization
        ));
        HttpStatus status = order.status() == OrderStatus.PAID
                ? HttpStatus.CREATED
                : HttpStatus.OK;
        return ResponseEntity.status(status).body(OrderDtos.OrderResponse.from(order));
    }

    @GetMapping("/{orderId}")
    public OrderDtos.OrderResponse getOrder(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID orderId
    ) {
        UUID customerId = cartIdentityResolver.requireCustomerId(jwt);
        return OrderDtos.OrderResponse.from(getOrderPort.getById(customerId, orderId));
    }

    @GetMapping("/me")
    public List<OrderDtos.OrderResponse> listMyOrders(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "20") int limit
    ) {
        UUID customerId = cartIdentityResolver.requireCustomerId(jwt);
        return getOrderPort.listForCustomer(customerId, limit).stream()
                .map(OrderDtos.OrderResponse::from)
                .toList();
    }
}
