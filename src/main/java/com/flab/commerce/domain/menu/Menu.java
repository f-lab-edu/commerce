package com.flab.commerce.domain.menu;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

  private Long id;

  private String name;

  private Long price;

  private String image;

  private Long storeId;

  private ZonedDateTime createDateTime;

  private ZonedDateTime modifyDateTime;
}
