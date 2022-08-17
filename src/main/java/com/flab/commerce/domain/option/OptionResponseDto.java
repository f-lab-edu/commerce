package com.flab.commerce.domain.option;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionResponseDto {

  private Long id;

  private String name;

  private BigInteger price;
}
