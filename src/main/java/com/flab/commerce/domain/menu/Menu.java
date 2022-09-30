package com.flab.commerce.domain.menu;

import com.flab.commerce.domain.menuoptiongroup.MenuOptionGroup;
import com.flab.commerce.exception.BadInputException;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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

  private BigInteger price;

  private String image;

  private Long storeId;

  private ZonedDateTime createDateTime;

  private ZonedDateTime modifyDateTime;

  private List<MenuOptionGroup> groups;

  public BigInteger calculateTotalPrice(int amount) {
    if (amount <= 0) {
      throw new BadInputException("주문 상품의 개수는 0이나 음수가 될 수 없습니다");
    }

    return price.multiply(BigInteger.valueOf(amount));
  }

  public List<MenuOption> getMenuOptions() {
    return this.groups.stream()
        .map(MenuOptionGroup::getOptions)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
