package com.flab.commerce.domain.menu;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuOptionGroup {

  private Long id;

  private List<MenuOption> options;
}
