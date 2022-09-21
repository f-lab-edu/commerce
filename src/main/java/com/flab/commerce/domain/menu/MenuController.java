package com.flab.commerce.domain.menu;

import com.flab.commerce.domain.store.StoreService;
import com.flab.commerce.security.owner.OwnerDetails;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;

  private final StoreService storeService;

  @PostMapping
  public ResponseEntity<Void> addMenu(@Valid @RequestBody MenuRegisterDto menuRegisterDto,
      @PathVariable Long storeId, @AuthenticationPrincipal OwnerDetails ownerDetails) {

    storeService.validateOwnerStore(ownerDetails.getOwner().getId(), storeId);

    Menu menu = MenuObjectMapper.INSTANCE.toEntity(menuRegisterDto, storeId);
    menuService.registerMenu(menu);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping
  public ResponseEntity<List<SearchMenuDto>> getMenus(@PathVariable Long storeId) {
    storeService.validateStoreExistence(storeId);

    List<Menu> menus = menuService.getMenus(storeId);

    return ResponseEntity.ok(MenuObjectMapper.INSTANCE.toDto(menus));
  }

  @DeleteMapping("/{menuId}")
  public ResponseEntity deleteMenu(@PathVariable Long storeId, @PathVariable Long menuId,
      @AuthenticationPrincipal OwnerDetails ownerDetails) {

    storeService.validateOwnerStore(ownerDetails.getOwner().getId(), storeId);

    menuService.validateMenu(menuId, storeId);

    menuService.deleteMenu(menuId, storeId);

    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{menuId}")
  public ResponseEntity patchMenu(@PathVariable Long storeId, @PathVariable Long menuId,
      @Valid @RequestBody MenuPatchDto menuPatchDto,
      @AuthenticationPrincipal OwnerDetails ownerDetails) {

    storeService.validateOwnerStore(ownerDetails.getOwner().getId(), storeId);

    menuService.validateMenu(menuId, storeId);

    Menu menu = MenuObjectMapper.INSTANCE.toEntity(menuPatchDto, menuId);

    menuService.patchMenu(menu);

    return ResponseEntity.ok().build();
  }
}
