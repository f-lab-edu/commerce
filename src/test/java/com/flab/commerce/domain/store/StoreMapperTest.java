package com.flab.commerce.domain.store;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.flab.commerce.domain.owner.Owner;
import com.flab.commerce.domain.owner.OwnerMapper;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

@MybatisTest
class StoreMapperTest {

    @Autowired
    private StoreMapper mapper;

@Autowired
    private OwnerMapper ownerMapper;

    @Test
    void 식당을_등록한다() {
        Owner owner = Owner.builder()
            .email("bgpark82@gmail.com")
            .password("1234")
            .name("박병길")
            .phone("0101231234")
            .createDateTime(ZonedDateTime.now())
            .modifyDateTime(ZonedDateTime.now())
            .build();
        ownerMapper.register(owner);

        Store store = createStore(owner.getId());

        int countInsertRow = mapper.register(store);

        assertThat(countInsertRow).isEqualTo(1);
    }

    @Test
    void 아이디가존재한다_true() {
        // Given
        Owner owner = Owner.builder()
            .email("bgpark82@gmail.com")
            .password("1234")
            .name("박병길")
            .phone("0101231234")
            .createDateTime(ZonedDateTime.now())
            .modifyDateTime(ZonedDateTime.now())
            .build();
        ownerMapper.register(owner);

        Store store = createStore(owner.getId());
        mapper.register(store);

        // When
        boolean exists = mapper.idExists(store.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void 아이디가존재한다_false_아이디가존재하지않는경우() {
        // When
        boolean exists = mapper.idExists(-1L);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void 아이디가존재한다_false_아이디가null일때() {
        // When
        boolean exists = mapper.idExists(null);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void 가가id와사장님id가존재한다_true() {
        // Given
        Owner owner = Owner.builder()
            .email("bgpark82@gmail.com")
            .password("1234")
            .name("박병길")
            .phone("0101231234")
            .createDateTime(ZonedDateTime.now())
            .modifyDateTime(ZonedDateTime.now())
            .build();
        ownerMapper.register(owner);

        Store store = createStore(owner.getId());
        mapper.register(store);

        // When
        Boolean exists = mapper.idAndOwnerIdExists(store.getId(), owner.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void 가가id와사장님d가존재한다_false_다른가게사장님() {
        // Given
        Owner ownerOfStore = Owner.builder()
            .email("bgpark82@gmail.com")
            .password("1234")
            .name("박병길")
            .phone("0101231234")
            .createDateTime(ZonedDateTime.now())
            .modifyDateTime(ZonedDateTime.now())
            .build();
        ownerMapper.register(ownerOfStore);

        Store store = createStore(ownerOfStore.getId());
        mapper.register(store);

        Owner owner = Owner.builder()
            .email("bgpark82@gmail.com")
            .password("1234")
            .name("박병길")
            .phone("0101231234")
            .createDateTime(ZonedDateTime.now())
            .modifyDateTime(ZonedDateTime.now())
            .build();
        ownerMapper.register(owner);

        // When
        Boolean exists = mapper.idAndOwnerIdExists(store.getId(), owner.getId());

        // Then
        assertThat(exists).isFalse();
    }

    private Store createStore(Long ownerId) {
        return Store.builder()
                .name("홍콩반점")
                .address("서울시 서초구 반포동")
                .phone("021231234")
                .description("중국집")
                .status(StoreStatus.OPEN)
                .createDateTime(ZonedDateTime.now())
                .modifyDateTime(ZonedDateTime.now())
                .ownerId(ownerId)
                .build();
    }
}
