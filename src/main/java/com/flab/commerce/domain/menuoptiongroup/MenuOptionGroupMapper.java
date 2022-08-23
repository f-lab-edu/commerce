package com.flab.commerce.domain.menuoptiongroup;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuOptionGroupMapper {

  int saveAll(List<MenuOptionGroup> menuOptionGroups);

  int deleteByMenuId(Long menuId);

  boolean menuIdAndOptionGroupIdExists(Long menuId, Long optionGroupId);

  MenuOptionGroup findById(Long id);

  List<MenuOptionGroup> findByMenuId(Long menuId);
}
