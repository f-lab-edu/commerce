package com.flab.commerce.domain.menuoptiongroup;

import com.flab.commerce.domain.menu.Menu;
import com.flab.commerce.domain.menu.MenuOption;
import com.flab.commerce.domain.optiongroup.OptionGroup;
import java.time.ZonedDateTime;
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

  private Long menuId;

  private Long optionGroupId;

  private List<Menu> menus;

  private OptionGroup optionGroup;

  private List<MenuOption> options;

  private ZonedDateTime createDateTime;

  private ZonedDateTime modifyDateTime;
}
