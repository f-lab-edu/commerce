package com.flab.commerce.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.commerce.domain.menu.Menu;
import com.flab.commerce.domain.menu.MenuMapper;
import com.flab.commerce.domain.owner.Owner;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.Store;
import com.flab.commerce.domain.store.StoreMapper;
import com.flab.commerce.domain.store.StoreStatus;
import com.flab.commerce.domain.user.User;
import com.flab.commerce.domain.user.UserMapper;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

@MybatisTest
class CartMapperTest {

  @Autowired
  CartMapper cartMapper;

  @Autowired
  UserMapper userMapper;

  @Autowired
  MenuMapper menuMapper;

  @Autowired
  StoreMapper storeMapper;

  @Autowired
  OwnerMapper ownerMapper;

  @Test
  void 메뉴추가() {
    // Given
    User user = getUser();
    Menu menu = getMenu();
    Cart cart = Cart.builder()
        .amount(1L)
        .userId(user.getId())
        .menuId(menu.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    cartMapper.register(cart);

    // Then
    Cart foundCart = cartMapper.findById(cart.getId());
    assertThat(foundCart).isNotNull();
    assertThat(foundCart.getAmount()).isEqualTo(cart.getAmount());
    assertThat(foundCart.getUserId()).isEqualTo(cart.getUserId());
    assertThat(foundCart.getMenuId()).isEqualTo(cart.getMenuId());
    assertThat(foundCart.getCreateDateTime()).isEqualTo(cart.getCreateDateTime());
    assertThat(foundCart.getModifyDateTime()).isEqualTo(cart.getModifyDateTime());
  }

  @Test
  void 메뉴추가_이미존재하는경우() {
    // Given
    User user = getUser();
    Menu menu = getMenu();
    Cart cart = Cart.builder()
        .amount(1L)
        .userId(user.getId())
        .menuId(menu.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    cartMapper.register(cart);

    // When
    Throwable throwable = Assertions.catchThrowable(() -> cartMapper.register(cart));

    // Then
    assertThat(throwable).isInstanceOf(DuplicateKeyException.class);
  }

  @Test
  void 사용자아이디와메뉴아이디가존재한다() {
    // Given
    User user = getUser();
    Menu menu = getMenu();
    Cart cart = Cart.builder()
        .amount(1L)
        .userId(user.getId())
        .menuId(menu.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    cartMapper.register(cart);

    // When
    boolean exists = cartMapper.userIdAndMenuIdExists(user.getId(), menu.getId());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void 사용자아이디와메뉴아이디가존재한다_존재하지않는경우() {
    // Given
    User user = getUser();
    Menu menu = getMenu();

    // When
    boolean exists = cartMapper.userIdAndMenuIdExists(user.getId(), menu.getId());

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void 사용자아이디와메뉴아이디로찾는다() {
    // Given
    User user = getUser();
    Menu menu = getMenu();
    Cart cart = Cart.builder()
        .amount(1L)
        .userId(user.getId())
        .menuId(menu.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    cartMapper.register(cart);

    // When
    Cart foundCart = cartMapper.findByUserIdAndMenuId(user.getId(), menu.getId());

    // Then
    assertThat(foundCart).isNotNull();
    assertThat(foundCart.getAmount()).isEqualTo(cart.getAmount());
    assertThat(foundCart.getUserId()).isEqualTo(cart.getUserId());
    assertThat(foundCart.getMenuId()).isEqualTo(cart.getMenuId());
    assertThat(foundCart.getCreateDateTime()).isEqualTo(cart.getCreateDateTime());
    assertThat(foundCart.getModifyDateTime()).isEqualTo(cart.getModifyDateTime());
  }

  @Test
  void 사용자아이디와메뉴아이디로찾는다_찾지못한경우() {
    // Given
    User user = getUser();
    Menu menu = getMenu();

    // When
    Cart foundCart = cartMapper.findByUserIdAndMenuId(user.getId(), menu.getId());

    // Then
    assertThat(foundCart).isNull();
  }

  @Test
  void 수량수정() {
    // Given
    User user = getUser();
    Menu menu = getMenu();
    long beforeAmount = 1L;
    long afterAmount = 3L;
    Cart cart = Cart.builder()
        .amount(beforeAmount)
        .userId(user.getId())
        .menuId(menu.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    cartMapper.register(cart);

    // When
    Cart updateCart = Cart.builder()
        .id(cart.getId())
        .amount(afterAmount)
        .modifyDateTime(ZonedDateTime.now())
        .build();
    cartMapper.updateAmount(updateCart);

    // Then
    Cart foundCart = cartMapper.findById(cart.getId());
    assertThat(foundCart).isNotNull();
    assertThat(foundCart.getAmount()).isNotEqualTo(beforeAmount);
    assertThat(foundCart.getAmount()).isEqualTo(afterAmount);
    assertThat(foundCart.getUserId()).isEqualTo(cart.getUserId());
    assertThat(foundCart.getMenuId()).isEqualTo(cart.getMenuId());
    assertThat(foundCart.getCreateDateTime()).isEqualTo(cart.getCreateDateTime());
    assertThat(foundCart.getModifyDateTime()).isNotEqualTo(cart.getModifyDateTime());
  }

  @Test
  void 아이디로찾기(){
    // Given
    User user = getUser();
    Menu menu = getMenu();
    Cart cart = Cart.builder()
        .amount(1L)
        .userId(user.getId())
        .menuId(menu.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    cartMapper.register(cart);

    // When
    Cart foundCart = cartMapper.findById(cart.getId());

    // Then
    assertThat(foundCart).isNotNull();
    assertThat(foundCart.getAmount()).isEqualTo(cart.getAmount());
    assertThat(foundCart.getUserId()).isEqualTo(cart.getUserId());
    assertThat(foundCart.getMenuId()).isEqualTo(cart.getMenuId());
    assertThat(foundCart.getCreateDateTime()).isEqualTo(cart.getCreateDateTime());
    assertThat(foundCart.getModifyDateTime()).isEqualTo(cart.getModifyDateTime());
  }

  @Test
  void 아이디로찾기_찾지못한경우(){
    // Given
    long id = 1L;

    // When
    Cart foundCart = cartMapper.findById(id);

    // Then
    assertThat(foundCart).isNull();
  }

  private User getUser() {
    User user = User.builder()
        .email("test@gmail.com")
        .name("홍길동")
        .zipcode("00000")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("1234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    userMapper.insertUser(user);
    return user;
  }

  private Menu getMenu() {
    Owner owner = Owner.builder()
        .email("bgpark82@gmail.com")
        .password("1234")
        .name("박병길")
        .phone("0101231234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store);

    Menu menu = Menu.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu);
    return menu;
  }
}