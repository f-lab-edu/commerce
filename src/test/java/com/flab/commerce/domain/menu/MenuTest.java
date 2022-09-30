package com.flab.commerce.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.commerce.exception.BadInputException;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class MenuTest {

  @Test
  void 가격을_계산한다() {
    Menu menu = createMenu(1_000L);

    BigInteger totalPrice = menu.calculateTotalPrice(2);

    assertThat(totalPrice).isEqualTo(BigInteger.valueOf(2_000L));
  }

  @Test
  void 상품_개수가_0_미만이면_에러를_발생한다() {
    Menu menu = createMenu(1_000L);

    assertThatThrownBy(() -> menu.calculateTotalPrice(-1))
        .isInstanceOf(BadInputException.class);
  }

  private Menu createMenu(long price) {
    return Menu.builder()
        .price(BigInteger.valueOf(price))
        .build();
  }
}
