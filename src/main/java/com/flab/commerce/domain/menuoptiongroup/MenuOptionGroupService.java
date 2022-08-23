package com.flab.commerce.domain.menuoptiongroup;

import com.flab.commerce.exception.BadInputException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuOptionGroupService {

  private final MenuOptionGroupMapper menuOptionGroupMapper;

  public void validateDuplicate(Long menuId, Long optionGroupId) {
    if (menuOptionGroupMapper.menuIdAndOptionGroupIdExists(menuId, optionGroupId)){
      throw new BadInputException("이미 존재하는 메뉴옵션그룹 입니다.");
    }
  }

  public void saveAfterDeletion(Long menuId, List<MenuOptionGroup> menuOptionGroups) {
    menuOptionGroupMapper.deleteByMenuId(menuId);
    menuOptionGroupMapper.saveAll(menuOptionGroups);
  }
}
