package com.vaultgame.order.infrastructure.web.dto;

import com.vaultgame.order.domain.cart.Cart;
import com.vaultgame.order.domain.cart.CartItem;
import com.vaultgame.order.domain.order.Order;
import com.vaultgame.order.domain.order.OrderLine;
import com.vaultgame.order.domain.order.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class OrderDtos {

    private OrderDtos() {
    }

    public record CartItemResponse(String productId, int quantity) {
        public static CartItemResponse from(CartItem item) {
            return new CartItemResponse(item.productId(), item.quantity());
        }
    }

    public record CartResponse(String cartKey, List<CartItemResponse> items) {
        public static CartResponse from(Cart cart) {
            return new CartResponse(
                    cart.cartKey(),
                    cart.items().stream().map(CartItemResponse::from).toList()
            );
        }
    }

    public record AddCartItemRequest(
            @NotBlank String productId,
            @NotNull @Min(1) Integer quantity
    ) {
    }

    public record UpdateCartItemRequest(@NotNull @Min(1) Integer quantity) {
    }

    public record PaymentMethodRequest(@NotBlank String type, @NotBlank String token) {
    }

    public record CheckoutRequest(
            @NotNull UUID shippingAddressId,
            @NotNull PaymentMethodRequest paymentMethod
    ) {
    }

    public record OrderLineResponse(
            String productId,
            String productName,
            BigDecimal unitPrice,
            int quantity,
            BigDecimal lineTotal
    ) {
        public static OrderLineResponse from(OrderLine line) {
            return new OrderLineResponse(
                    line.productId(),
                    line.productName(),
                    line.unitPrice(),
                    line.quantity(),
                    line.lineTotal()
            );
        }
    }

    public record OrderResponse(
            UUID id,
            OrderStatus status,
            String currency,
            BigDecimal totalAmount,
            String paymentTransactionId,
            UUID shippingAddressId,
            List<OrderLineResponse> lines,
            String createdAt
    ) {
        public static OrderResponse from(Order order) {
            return new OrderResponse(
                    order.id(),
                    order.status(),
                    order.currency(),
                    order.totalAmount(),
                    order.paymentTransactionId(),
                    order.shippingAddressId(),
                    order.lines().stream().map(OrderLineResponse::from).toList(),
                    order.createdAt().toString()
            );
        }
    }
}
