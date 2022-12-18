package com.flab.commerce.domain.cart;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

  private Long id;

  private Long amount;

  private Long userId;

  private Long menuId;

  private ZonedDateTime createDateTime;

  private ZonedDateTime modifyDateTime;

  public void addAmount(Long amount) {
    if (amount < 1) {
      throw new IllegalArgumentException("amount는 1보다 작을 수 없습니다.");
    }
    this.amount += amount;
  }
}
