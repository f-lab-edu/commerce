package com.flab.commerce.domain.owner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class})
class OwnerServiceTest {

    @InjectMocks
    private OwnerService ownerService;

    @Mock
    private OwnerMapper ownerMapper;

    @Test
    void 사장님이_회원가입한다() {
        given(ownerMapper.register(any(Owner.class))).willReturn(1);

        boolean result = ownerService.register(createRequest().build());

        assertThat(result).isTrue();
    }

    @Test
    void 이메일_존재여부를_확인한다() {
        given(ownerMapper.findByEmail(anyString())).willReturn(new Owner());

        assertThatThrownBy(() -> ownerService.register(createRequest().email("bgpark@gmail.com").build()))
                .isInstanceOf(RuntimeException.class);
    }

    private OwnerRegisterDto.OwnerRegisterDtoBuilder createRequest() {
        return OwnerRegisterDto
                .builder()
                .email("bgpark82@gmail.com")
                .password("1234")
                .name("박병길")
                .phone("01045808682");
    }
}
