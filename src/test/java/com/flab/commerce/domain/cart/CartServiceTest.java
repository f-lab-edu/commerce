package com.flab.commerce.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

  @InjectMocks
  private CartService cartService;

  @Mock
  private CartMapper cartMapper;

  @Test
  void 상품추가_상품이존재할경우() {
    // Given
    long addAmount = 1L;
    long amount = 1L;
    Cart cart = Cart.builder()
        .amount(addAmount)
        .userId(1L)
        .menuId(1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    Cart foundCart = Cart.builder().amount(amount).build();

    // When
    when(cartMapper.userIdAndMenuIdExists(cart.getUserId(), cart.getMenuId())).thenReturn(true);
    when(cartMapper.findByUserIdAndMenuId(cart.getUserId(), cart.getMenuId())).thenReturn(
        foundCart);
    cartService.addMenu(cart);

    // Then
    verify(cartMapper).findByUserIdAndMenuId(cart.getUserId(), cart.getMenuId());
    verify(cartMapper).updateAmount(foundCart);
    verify(cartMapper, never()).register(any());
    assertThat(foundCart.getAmount()).isEqualTo(amount + addAmount);
  }

  @Test
  void 상품추가_상품이존재하지않는경우() {
    // Given
    Cart cart = Cart.builder()
        .amount(1L)
        .userId(1L)
        .menuId(1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    when(cartMapper.userIdAndMenuIdExists(any(), any())).thenReturn(false);
    cartService.addMenu(cart);

    // Then
    verify(cartMapper, never()).findByUserIdAndMenuId(any(), any());
    verify(cartMapper, never()).updateAmount(any());
    verify(cartMapper).register(cart);
  }
}