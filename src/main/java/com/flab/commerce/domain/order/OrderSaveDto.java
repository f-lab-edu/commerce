package com.flab.commerce.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSaveDto {

  private Long menuId;

  private Long userId;

  private int amount;

  private String address;

  private String addressDetail;

  private String zipCode;

  private String phone;
}
