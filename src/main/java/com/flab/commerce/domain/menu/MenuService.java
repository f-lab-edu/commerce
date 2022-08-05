package com.flab.commerce.domain.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuMapper menuMapper;

  public boolean registerMenu(Menu menu) {
    return menuMapper.register(menu) == 1;
  }
}