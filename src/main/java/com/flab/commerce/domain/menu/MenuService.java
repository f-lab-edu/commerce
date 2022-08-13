package com.flab.commerce.domain.menu;

import com.flab.commerce.exception.BadInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuMapper menuMapper;

  public void validateMenu(Long menuId, Long storeId) {
    if (!menuMapper.idExists(menuId)){
      throw new BadInputException("해당 메뉴는 존재하지 않습니다.");
    }
    if (!menuMapper.idAndStoreIdExists(menuId, storeId)){
      throw new AccessDeniedException("다른 가게의 메뉴입니다.");
    }
  }

  public void deleteMenu(Long menuId, Long storeId) {
    menuMapper.deleteByIdAndStoreId(menuId, storeId);
  }

  public void patchMenu(Menu menu){
    menuMapper.patch(menu);
  }
}