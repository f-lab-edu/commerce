package com.flab.commerce.domain.menu;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuMapper menuMapper;

  public List<Menu> getMenus(Long storeId) {
    return menuMapper.findByStoreId(storeId);
  }
}
