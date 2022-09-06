package com.flab.commerce.domain.order;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenuOptionSaveDto {

  private String optionGroupName;

  private String optionName;

  private BigInteger price;
}
