package com.flab.commerce.domain.menu;

import com.flab.commerce.exception.BadInputException;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

  private Long id;

  private BigDecimal price;

  private List<MenuOptionGroup> groups;

  public BigDecimal calculateTotalPrice(int amount) {
    if (amount <= 0) {
      throw new BadInputException("주문 상품의 개수는 0이나 음수가 될 수 없습니다");
    }

    return price.multiply(BigDecimal.valueOf(amount));
  }
}
