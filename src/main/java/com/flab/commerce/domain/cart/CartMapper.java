package com.flab.commerce.domain.cart;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper {

  void register(Cart cart);

  void updateAmount(Cart cart);

  void deleteById(Long id);

  void deleteByUserId(Long userId);

  boolean userIdAndMenuIdExists(Long userId, Long menuId);

  Cart findByUserIdAndMenuId(Long userId, Long menuId);

  Cart findById(Long id);
}
