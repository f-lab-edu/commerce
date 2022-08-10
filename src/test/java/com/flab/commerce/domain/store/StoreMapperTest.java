package com.flab.commerce.domain.store;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
class StoreMapperTest {

    @Autowired
    private StoreMapper mapper;

    @Test
    void 식당을_등록한다() {
        Store store = createStore();

        int countInsertRow = mapper.register(store);

        assertThat(countInsertRow).isEqualTo(1);
    }

    private Store createStore() {
        return Store.builder()
                .name("홍콩반점")
                .address("서울시 서초구 반포동")
                .phone("021231234")
                .description("중국집")
                .status(StoreStatus.OPEN)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .ownerId(1L)
                .build();
    }
}
