package com.flab.commerce.domain.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreMapper storeMapper;

    @Test
    void 식당을_등록한다() {
        StoreRegisterDto registerRequest = createRegisterRequest();
        given(storeMapper.register(any(Store.class))).willReturn(1);

        boolean isRegistered = storeService.register(registerRequest, 1L);

        assertThat(isRegistered).isTrue();
    }

    private StoreRegisterDto createRegisterRequest() {
        return StoreRegisterDto.builder()
                .name("홍콩반점")
                .address("서울시 서초구 반포동")
                .phone("021231234")
                .description("중국집")
                .build();
    }
}
