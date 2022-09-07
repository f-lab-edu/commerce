package com.flab.commerce.domain.menu;

import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper {

  int register(Menu menu);

  Menu findById(Long id);

  List<Menu> findByStoreId(Long storeId);

  int deleteByIdAndStoreId(Long id, Long storeId);

  boolean idExists(Long id);

  boolean idAndStoreIdExists(Long id, Long storeId);

  int patch(Menu menu);

  List<Menu> findByIdIn(Set<Long> ids);
}
