package com.flab.commerce.domain.menu;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;

  @PostMapping
  public ResponseEntity<Void> addMenu(@Valid @RequestBody MenuRegisterDto menuRegisterDto) {
    Menu menu = MenuObjectMapper.INSTANCE.toEntity(menuRegisterDto);
    if (!menuService.registerMenu(menu)){
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
