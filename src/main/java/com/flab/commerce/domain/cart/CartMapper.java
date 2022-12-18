package com.flab.commerce.domain.cart;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper {

  void register(Cart cart);

  boolean userIdAndMenuIdExists(Long userId, Long menuId);

  void updateAmount(Cart cart);

  Cart findByUserIdAndMenuId(Long userId, Long menuId);

  Cart findById(Long id);
}
