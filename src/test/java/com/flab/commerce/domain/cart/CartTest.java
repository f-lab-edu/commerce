package com.flab.commerce.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CartTest {

  @Test
  void 수량추가(){
    // Given
    Cart cart = Cart.builder()
        .amount(1L)
        .build();

    // When
    cart.addAmount(3L);

    // Then
    assertThat(cart.getAmount()).isEqualTo(4L);
  }

  @Test
  void 수량추가_음수를추가할경우(){
    // Given
    Cart cart = Cart.builder()
        .amount(1L)
        .build();

    // When
    Throwable throwable = Assertions.catchThrowable(() -> cart.addAmount(-1L));

    // Then
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
  }
}