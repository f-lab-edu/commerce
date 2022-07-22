package com.flab.commerce.domain.owner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.owner.OwnerRegisterDto.OwnerRegisterDtoBuilder;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private OwnerService ownerService;

    @Test
    void 회원가입한다() throws Exception {
        String body = mapper.writeValueAsString(createOwner().build());

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("emailEdgeCase")
    void 회원가입_이메일_확인(String email) throws Exception {
        String body = mapper.writeValueAsString(createOwner().email(email).build());

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("passwordEdgeCase")
    void 회원가입_비밀번호_확인(String password) throws Exception {
        String body = mapper.writeValueAsString(createOwner().password(password).build());

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("nameEdgeCase")
    void 회원가입_이름_확인(String name) throws Exception {
        String body = mapper.writeValueAsString(createOwner().name(name).build());

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("phoneEdgeCase")
    void 회원가입_전화번호_확인(String phone) throws Exception {
        String body = mapper.writeValueAsString(createOwner().phone(phone).build());

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    private OwnerRegisterDtoBuilder createOwner() {
        return OwnerRegisterDto.builder()
                .email("bgpark82@gmail.com")
                .password("12345678")
                .name("박병길")
                .phone("0101231234");
    }

    static Stream<String> emailEdgeCase() {
        return concatCases("email");
    }

    static Stream<String> nameEdgeCase() {
        return concatCases("김");
    }

    static Stream<String> passwordEdgeCase() {
        return concatCases("1234");
    }

    static Stream<String> phoneEdgeCase() {
        return concatCases("010111111");
    }

    private static Stream<String> concatCases(String... streams) {
        return Stream.concat(blankOrNullStrings(), Stream.of(streams));
    }

    static Stream<String> blankOrNullStrings() {
        return Stream.of("", " ", null);
    }
}
