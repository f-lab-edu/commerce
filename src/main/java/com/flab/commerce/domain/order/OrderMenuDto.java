package com.flab.commerce.domain.order;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenuDto {

  @NotNull
  private Long menuId;

  @Min(value = 0)
  private int amount;

  private List<Long> optionIds;
}
