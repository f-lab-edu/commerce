package com.flab.commerce.domain.owner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
class OwnerMapperTest {

    @Autowired
    OwnerMapper ownerMapper;

    @Test
    void 사장님이_회원가입한다() {
        Owner owner = createOwner();

        int countInsertRow = ownerMapper.register(owner);

        assertThat(countInsertRow).isEqualTo(1);
    }

    @Test
    void 이메일로_사장님을_조회한다() {
        ownerMapper.register(createOwner());
        String email = "bgpark82@gmail.com";

        Owner owner = ownerMapper.findByEmail(email);

        assertThat(owner.getEmail()).isEqualTo(email);
    }

    private Owner createOwner() {
        return Owner.builder()
                .email("bgpark82@gmail.com")
                .password("1234")
                .name("박병길")
                .phone("0101231234")
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();
    }
}
