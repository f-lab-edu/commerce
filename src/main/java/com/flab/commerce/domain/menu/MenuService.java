package com.flab.commerce.domain.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuMapper menuMapper;

  public void registerMenu(Menu menu) {
    menuMapper.register(menu);
  }
}