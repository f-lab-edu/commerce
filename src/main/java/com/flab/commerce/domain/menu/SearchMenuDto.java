package com.flab.commerce.domain.menu;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SearchMenuDto {

  private Long id;

  private String name;

  private BigDecimal price;

  private String image;

  private Long storeId;

  private ZonedDateTime createDateTime;

  private ZonedDateTime modifyDateTime;
}
