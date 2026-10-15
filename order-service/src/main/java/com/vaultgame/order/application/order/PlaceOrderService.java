package com.vaultgame.order.application.order;

import com.vaultgame.order.application.cart.CartService;
import com.vaultgame.order.domain.cart.Cart;
import com.vaultgame.order.domain.cart.CartItem;
import com.vaultgame.order.domain.cart.EmptyCartException;
import com.vaultgame.order.domain.exception.CatalogProductNotFoundException;
import com.vaultgame.order.domain.exception.InactiveProductException;
import com.vaultgame.order.domain.order.IdempotencyConflictException;
import com.vaultgame.order.domain.order.Order;
import com.vaultgame.order.domain.order.OrderLine;
import com.vaultgame.order.domain.order.OrderRepository;
import com.vaultgame.order.domain.order.OrderStatus;
import com.vaultgame.order.domain.port.CatalogPort;
import com.vaultgame.order.domain.port.CustomerPort;
import com.vaultgame.order.domain.port.EmailPort;
import com.vaultgame.order.domain.port.InventoryPort;
import com.vaultgame.order.domain.port.PaymentPort;
import com.vaultgame.order.domain.port.model.CatalogProduct;
import com.vaultgame.order.domain.port.model.PaymentRequest;
import com.vaultgame.order.domain.port.model.PaymentResult;
import com.vaultgame.order.domain.port.model.ReservationRequestLine;
import com.vaultgame.order.domain.port.model.StockReservationResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlaceOrderService {

    private static final String DEFAULT_CURRENCY = "BRL";

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final CatalogPort catalogPort;
    private final InventoryPort inventoryPort;
    private final PaymentPort paymentPort;
    private final EmailPort emailPort;
    private final CustomerPort customerPort;

    public PlaceOrderService(
            CartService cartService,
            OrderRepository orderRepository,
            CatalogPort catalogPort,
            InventoryPort inventoryPort,
            PaymentPort paymentPort,
            EmailPort emailPort,
            CustomerPort customerPort
    ) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.catalogPort = catalogPort;
        this.inventoryPort = inventoryPort;
        this.paymentPort = paymentPort;
        this.emailPort = emailPort;
        this.customerPort = customerPort;
    }

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
            Optional<Order> existing = orderRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return resolveIdempotentReplay(existing.get(), requestHash, idempotencyKey);
            }
        }

        Cart cart = cartService.getCart(command.cartKey());
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
        orderRepository.save(pendingOrder);

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
            Order paid = orderRepository.save(
                    pendingOrder.withPayment(payment.transactionId(), OrderStatus.PAID)
            );
            sendConfirmationEmail(command, paid);
            cartService.clear(command.cartKey());
            return paid;
        }

        inventoryPort.release(reservation.reservationId());
        OrderStatus failedStatus = payment.status() == PaymentResult.PaymentStatus.PENDING
                ? OrderStatus.PENDING_PAYMENT
                : OrderStatus.PAYMENT_FAILED;
        return orderRepository.save(
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
