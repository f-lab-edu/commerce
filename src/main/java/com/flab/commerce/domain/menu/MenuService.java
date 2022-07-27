package com.flab.commerce.domain.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuMapper menuMapper;

  public boolean registerMenu(Menu menu) {
    if (menuMapper.insertMenu(menu) == 0) {
      return false;
    }
    return true;
  }
}
