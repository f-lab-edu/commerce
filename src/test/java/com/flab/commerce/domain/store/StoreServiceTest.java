package com.flab.commerce.domain.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flab.commerce.exception.BadInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

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

  @Test
  void 식당검증하다_void() {
    // When
    when(storeMapper.idExists(any())).thenReturn(true);
    when(storeMapper.idAndOwnerIdExists(any(), any())).thenReturn(true);

    storeService.validateOwnerStore(1L, 1L);

    // Then
    verify(storeMapper).idExists(any());
    verify(storeMapper).idAndOwnerIdExists(any(), any());
  }

    @Test
    void 식당검증하다_throw_가게id가존재하지않다() {
        // When
        when(storeMapper.idExists(any())).thenReturn(false);

        Throwable throwable = catchThrowableOfType(() -> storeService.validateOwnerStore(1L, 1L), BadInputException.class);

        // Then
        assertThat(throwable).isInstanceOf(BadInputException.class);
        verify(storeMapper).idExists(any());
        verify(storeMapper,never()).idAndOwnerIdExists(any(), any());
    }
    @Test
    void 식당검증하다_throw_가게id와사장니id가존재하지않다() {
        // When
        when(storeMapper.idExists(any())).thenReturn(true);
        when(storeMapper.idAndOwnerIdExists(any(), any())).thenReturn(false);

        Throwable throwable = catchThrowableOfType(() -> storeService.validateOwnerStore(1L, 1L), AccessDeniedException.class);

        // Then
        assertThat(throwable).isInstanceOf(AccessDeniedException.class);
        verify(storeMapper).idExists(any());
        verify(storeMapper).idAndOwnerIdExists(any(), any());
    }
}
