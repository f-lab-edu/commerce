package com.flab.commerce.domain.price;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {

  private Long id;

  private BigInteger price;

  private Long orderId;
}
