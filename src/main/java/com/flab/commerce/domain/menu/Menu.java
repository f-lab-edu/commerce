package com.flab.commerce.domain.menu;

import java.math.BigDecimal;
import java.util.List;
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

  private BigDecimal price;

  private List<MenuOptionGroup> groups;
}
