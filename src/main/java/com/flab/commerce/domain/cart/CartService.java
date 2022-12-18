package com.flab.commerce.domain.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartMapper cartMapper;

  public void addMenu(Cart cart) {
    if (cartMapper.userIdAndMenuIdExists(cart.getUserId(), cart.getMenuId())) {
      Cart findCart = cartMapper.findByUserIdAndMenuId(cart.getUserId(), cart.getMenuId());
      findCart.addAmount(cart.getAmount());
      cartMapper.updateAmount(findCart);
    } else {
      cartMapper.register(cart);
    }
  }

  public void updateAmount(Cart cart) {
    Cart foundCart = cartMapper.findById(cart.getId());
    if (!foundCart.getUserId().equals(cart.getUserId())) {
      throw new IllegalArgumentException("해당 사용자의 장바구니가 아닙니다.");
    }
    cartMapper.updateAmount(cart);
  }
}
