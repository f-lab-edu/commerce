package com.flab.commerce.domain.menu;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper {

  int insertMenu(Menu menu);

  Menu findById(Long id);
}
