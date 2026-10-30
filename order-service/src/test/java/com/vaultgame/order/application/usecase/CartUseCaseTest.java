package com.vaultgame.order.application.usecase;

import com.vaultgame.order.domain.model.cart.Cart;
import com.vaultgame.order.domain.model.cart.CartItem;
import com.vaultgame.order.application.port.out.CartRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartUseCaseTest {

    @Mock
    private CartRepositoryPort cartRepositoryPort;

    private CartUseCase cartUseCase;

    @BeforeEach
    void setUp() {
        cartUseCase = new CartUseCase(cartRepositoryPort);
    }

    @Test
    void merge_sumsQuantitiesAndDeletesGuestCart() {
        String userKey = "cart:user:11111111-1111-1111-1111-111111111111";
        String guestKey = "cart:guest:22222222-2222-2222-2222-222222222222";
        when(cartRepositoryPort.findByKey(userKey)).thenReturn(Optional.of(
                new Cart(userKey, List.of(new CartItem("p1", 1)))
        ));
        when(cartRepositoryPort.findByKey(guestKey)).thenReturn(Optional.of(
                new Cart(guestKey, List.of(new CartItem("p1", 2), new CartItem("p2", 1)))
        ));
        doNothing().when(cartRepositoryPort).save(any());

        Cart merged = cartUseCase.merge(userKey, guestKey);

        assertThat(merged.items()).hasSize(2);
        assertThat(merged.items().stream().filter(i -> i.productId().equals("p1")).findFirst())
                .get()
                .extracting(CartItem::quantity)
                .isEqualTo(3);
        verify(cartRepositoryPort).delete(guestKey);
        ArgumentCaptor<Cart> saved = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepositoryPort).save(saved.capture());
        assertThat(saved.getValue().cartKey()).isEqualTo(userKey);
    }
}
