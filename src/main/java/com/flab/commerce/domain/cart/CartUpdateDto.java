package com.flab.commerce.domain.cart;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartUpdateDto {

  @NotNull
  private Long id;

  @NotNull
  @Min(value = 1)
  private Long amount;
}
