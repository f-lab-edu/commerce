package com.flab.commerce.domain.menu;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper {

  Menu findById(Long id);
}
