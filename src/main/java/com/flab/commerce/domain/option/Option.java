package com.flab.commerce.domain.option;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Option {

  private Long id;

  private String name;

  private BigInteger price;

  private Long optionGroupId;

  private ZonedDateTime createDateTime;

  private ZonedDateTime modifyDateTime;
}
