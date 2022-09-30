package com.flab.commerce.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

  private LocalDateTime createDateTime;

  private LocalDateTime updateDateTime;

  private Long userId;
}
