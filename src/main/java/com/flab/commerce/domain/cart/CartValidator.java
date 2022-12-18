package com.flab.commerce.domain.cart;

import com.flab.commerce.domain.menu.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CartValidator implements Validator {

  private final MenuMapper menuMapper;

  @Override
  public boolean supports(Class<?> clazz) {
    return clazz.equals(CartRegisterDto.class);
  }

  @Override
  public void validate(Object target, Errors errors) {
    CartRegisterDto cartRegisterDto = (CartRegisterDto) target;

    if (!menuMapper.idExists(cartRegisterDto.getMenuId())) {
      errors.rejectValue("menuId", "invalid.menuId", new Object[]{cartRegisterDto.getMenuId()},
          "메뉴가 존재하지 않습니다.");
    }
  }
}
