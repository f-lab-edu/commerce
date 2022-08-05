package com.flab.commerce.domain.menu;

import com.flab.commerce.domain.store.StoreMapper;
import com.flab.commerce.security.owner.OwnerDetails;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
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

  private final StoreMapper storeMapper;

  @PostMapping
  public ResponseEntity addMenu(@Valid @RequestBody MenuRegisterDto menuRegisterDto,
      @PathVariable Long storeId, Errors errors,
      @AuthenticationPrincipal OwnerDetails ownerDetails) {

    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    if (!storeMapper.idExists(storeId)) {
      return ResponseEntity.badRequest().body("해당 가게는 존재하지 않습니다.");
    }

    if (!storeMapper.idAndOwnerIdExists(storeId, ownerDetails.getOwner().getId())) {
      return new ResponseEntity("해당 가게는 사장님에 속해있지 않습니다.", HttpStatus.UNAUTHORIZED);
    }

    Menu menu = MenuObjectMapper.INSTANCE.toEntity(menuRegisterDto, storeId);
    if (!menuService.registerMenu(menu)) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity(HttpStatus.CREATED);
  }
}
