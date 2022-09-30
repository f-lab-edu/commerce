package com.flab.commerce.domain.order;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

  private Long id;

  private BigInteger totalPrice;

  private OrderStatus status;

  private String menuOptions;

  private ZonedDateTime createDateTime;

  private ZonedDateTime modifyDateTime;

  private Long userId;
}
