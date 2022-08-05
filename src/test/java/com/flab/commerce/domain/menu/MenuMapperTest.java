package com.flab.commerce.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.flab.commerce.domain.owner.Owner;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.Store;
import com.flab.commerce.domain.store.StoreMapper;
import com.flab.commerce.domain.store.StoreStatus;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
class MenuMapperTest {

  @Autowired
  MenuMapper menuMapper;

  @Autowired
  StoreMapper storeMapper;

  @Autowired
  OwnerMapper ownerMapper;

  @Test
  void 메뉴등록_성공() {
    // Given
    Owner owner = Owner.builder()
        .email("bgpark82@gmail.com")
        .password("1234")
        .name("박병길")
        .phone("0101231234")
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store);

    Menu menu = Menu.builder()
        .name("돈까스")
        .price(10000L)
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    int insertCount = menuMapper.register(menu);

    // Then
    assertThat(insertCount).isOne();
    Menu foundMenu = menuMapper.findById(menu.getId());
    assertThat(foundMenu.getId()).isEqualTo(menu.getId());
    assertThat(foundMenu.getStoreId()).isEqualTo(menu.getStoreId());
  }

  @Test
  void 메뉴등록_실패_가게에없는ID인경우() {
    // Given
    Menu menu = Menu.builder()
        .name("돈까스")
        .price(10000L)
        .image("image")
        .storeId(-1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    Throwable throwable = catchThrowableOfType(
        () -> menuMapper.register(menu), DataIntegrityViolationException.class);

    // Then
    assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
  }
}