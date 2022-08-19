package com.flab.commerce.domain.menu;

import com.flab.commerce.domain.store.StoreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;

  private final StoreService storeService;

  @GetMapping
  public ResponseEntity<List<SearchMenuDto>> getMenus(@PathVariable Long storeId) {
    storeService.validateStoreExistence(storeId);

    List<Menu> menus = menuService.getMenus(storeId);

    return ResponseEntity.ok(MenuObjectMapper.INSTANCE.toDto(menus));
  }
}
