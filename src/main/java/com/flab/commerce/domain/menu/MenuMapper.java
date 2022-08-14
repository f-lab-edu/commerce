package com.flab.commerce.domain.menu;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper {

  int register(Menu menu);

  Menu findById(Long id);
}
