package com.vaultgame.order.application.usecase;

import com.vaultgame.order.application.port.in.CartPort;
import com.vaultgame.order.application.port.in.PlaceOrderPort;
import com.vaultgame.order.domain.model.cart.Cart;
import com.vaultgame.order.domain.model.cart.CartItem;
import com.vaultgame.order.domain.exception.EmptyCartException;
import com.vaultgame.order.domain.exception.CatalogProductNotFoundException;
import com.vaultgame.order.domain.exception.InactiveProductException;
import com.vaultgame.order.domain.exception.IdempotencyConflictException;
import com.vaultgame.order.domain.model.order.Order;
import com.vaultgame.order.domain.model.order.OrderLine;
import com.vaultgame.order.application.port.out.OrderRepositoryPort;
import com.vaultgame.order.domain.model.order.OrderStatus;
import com.vaultgame.order.application.port.out.CatalogPort;
import com.vaultgame.order.application.port.out.CustomerPort;
import com.vaultgame.order.application.port.out.EmailPort;
import com.vaultgame.order.application.port.out.InventoryPort;
import com.vaultgame.order.application.port.out.PaymentPort;
import com.vaultgame.order.application.port.out.model.CatalogProduct;
import com.vaultgame.order.application.port.out.model.PaymentRequest;
import com.vaultgame.order.application.port.out.model.PaymentResult;
import com.vaultgame.order.application.port.out.model.ReservationRequestLine;
import com.vaultgame.order.application.port.out.model.StockReservationResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlaceOrderUseCase implements PlaceOrderPort {

    private static final String DEFAULT_CURRENCY = "BRL";

    private final CartPort cartPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final CatalogPort catalogPort;
    private final InventoryPort inventoryPort;
    private final PaymentPort paymentPort;
    private final EmailPort emailPort;
    private final CustomerPort customerPort;

    public PlaceOrderUseCase(
            CartPort cartPort,
            OrderRepositoryPort orderRepositoryPort,
            CatalogPort catalogPort,
            InventoryPort inventoryPort,
            PaymentPort paymentPort,
            EmailPort emailPort,
            CustomerPort customerPort
    ) {
        this.cartPort = cartPort;
        this.orderRepositoryPort = orderRepositoryPort;
        this.catalogPort = catalogPort;
        this.inventoryPort = inventoryPort;
        this.paymentPort = paymentPort;
        this.emailPort = emailPort;
        this.customerPort = customerPort;
    }

    @Override
    public Order checkout(CheckoutCommand command) {
        String requestHash = CheckoutRequestHashCalculator.hash(
                command.customerId(),
                command.cartKey(),
                command.shippingAddressId(),
                command.paymentMethodType(),
                command.paymentMethodToken()
        );

        String idempotencyKey = normalizeIdempotencyKey(command.idempotencyKey());
        if (idempotencyKey != null) {
            Optional<Order> existing = orderRepositoryPort.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return resolveIdempotentReplay(existing.get(), requestHash, idempotencyKey);
            }
        }

        Cart cart = cartPort.getCart(command.cartKey());
        if (cart.isEmpty()) {
            throw new EmptyCartException();
        }

        List<String> productIds = cart.items().stream().map(CartItem::productId).toList();
        Map<String, CatalogProduct> catalogProducts = catalogPort.getProducts(productIds).stream()
                .collect(Collectors.toMap(CatalogProduct::id, Function.identity()));

        for (String productId : productIds) {
            CatalogProduct product = catalogProducts.get(productId);
            if (product == null) {
                throw new CatalogProductNotFoundException(productId);
            }
            if (!product.active()) {
                throw new InactiveProductException(productId);
            }
        }

        List<OrderLine> orderLines = cart.items().stream()
                .map(item -> {
                    CatalogProduct product = catalogProducts.get(item.productId());
                    return OrderLine.create(
                            product.id(),
                            product.name(),
                            product.price(),
                            item.quantity()
                    );
                })
                .toList();

        BigDecimal total = orderLines.stream()
                .map(OrderLine::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        UUID orderId = UUID.randomUUID();
        List<ReservationRequestLine> reservationLines = cart.items().stream()
                .map(item -> new ReservationRequestLine(item.productId(), item.quantity()))
                .toList();

        StockReservationResult reservation = inventoryPort.reserve(orderId.toString(), reservationLines);

        Order pendingOrder = Order.createPendingPayment(
                orderId,
                command.customerId(),
                command.cartKey(),
                DEFAULT_CURRENCY,
                total,
                command.shippingAddressId(),
                idempotencyKey,
                requestHash,
                orderLines,
                reservation.reservationId()
        );
        orderRepositoryPort.save(pendingOrder);

        PaymentResult payment = paymentPort.createPayment(
                new PaymentRequest(
                        orderId,
                        total,
                        DEFAULT_CURRENCY,
                        command.paymentMethodType(),
                        command.paymentMethodToken()
                ),
                idempotencyKey
        );

        if (payment.status() == PaymentResult.PaymentStatus.APPROVED) {
            inventoryPort.commit(reservation.reservationId());
            Order paid = orderRepositoryPort.save(
                    pendingOrder.withPayment(payment.transactionId(), OrderStatus.PAID)
            );
            sendConfirmationEmail(command, paid);
            cartPort.clear(command.cartKey());
            return paid;
        }

        inventoryPort.release(reservation.reservationId());
        OrderStatus failedStatus = payment.status() == PaymentResult.PaymentStatus.PENDING
                ? OrderStatus.PENDING_PAYMENT
                : OrderStatus.PAYMENT_FAILED;
        return orderRepositoryPort.save(
                pendingOrder.withPayment(payment.transactionId(), failedStatus)
        );
    }

    private void sendConfirmationEmail(CheckoutCommand command, Order order) {
        var profile = customerPort.getProfile(command.customerId(), command.bearerToken());
        emailPort.sendOrderConfirmed(profile.email(), order.id(), profile.fullName());
    }

    private Order resolveIdempotentReplay(Order existing, String requestHash, String idempotencyKey) {
        if (existing.requestHash() != null && !existing.requestHash().equals(requestHash)) {
            throw new IdempotencyConflictException(idempotencyKey);
        }
        return existing;
    }

    private static String normalizeIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return null;
        }
        return idempotencyKey.trim();
    }
}
