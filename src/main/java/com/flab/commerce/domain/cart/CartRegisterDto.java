package com.flab.commerce.domain.cart;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartRegisterDto {

  @NotNull
  @Min(value = 1)
  private Long amount;

  @NotNull
  private Long menuId;
}
