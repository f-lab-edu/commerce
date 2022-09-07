package com.flab.commerce.domain.order;

import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenuSaveDto {

  private String name;

  private BigInteger price;

  private BigInteger totalPrice;

  private int amount;

  private List<OrderMenuOptionSaveDto> orderMenuOptionSaveDtos;
}
