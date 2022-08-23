package com.flab.commerce.domain.menuoptiongroup;

import com.flab.commerce.domain.menu.MenuService;
import com.flab.commerce.domain.optiongroup.OptionGroupService;
import com.flab.commerce.domain.store.StoreService;
import com.flab.commerce.security.owner.OwnerDetails;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores/{storeId}/menu-option-groups")
@RequiredArgsConstructor
public class MenuOptionGroupController {

  private final StoreService storeService;

  private final MenuService menuService;

  private final OptionGroupService optionGroupService;

  private final MenuOptionGroupService menuOptionGroupService;

  @PostMapping
  public ResponseEntity<Void> saveAll(@PathVariable Long storeId,
      @AuthenticationPrincipal OwnerDetails ownerDetails,
      @RequestBody MenuOptionGroupRequestDto menuOptionGroupRequestDto) {
    Long menuId = menuOptionGroupRequestDto.getMenuId();
    storeService.validateOwnerStore(ownerDetails.getOwner().getId(), storeId);
    menuService.validateMenu(menuId, storeId);

    for (Long optionGroupId : menuOptionGroupRequestDto.getOptionGroupIds()) {
      optionGroupService.validateOptionGroupStore(optionGroupId, storeId);
      menuOptionGroupService.validateDuplicate(menuId, optionGroupId);
    }

    List<MenuOptionGroup> menuOptionGroups = new LinkedList<>();
    for (Long optionGroupId : menuOptionGroupRequestDto.getOptionGroupIds()) {
      MenuOptionGroup menuOptionGroup = MenuOptionGroupObjectMapper.INSTANCE.toEntity(menuId,
          optionGroupId);
      menuOptionGroups.add(menuOptionGroup);
    }

    menuOptionGroupService.saveAfterDeletion(menuId, menuOptionGroups);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
