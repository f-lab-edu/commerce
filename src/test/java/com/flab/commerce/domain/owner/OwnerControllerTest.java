package com.flab.commerce.domain.owner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        String body = mapper.writeValueAsString(createOwner());

        mvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private Owner createOwner() {
        return Owner.builder()
                .email("bgpark82@gmail.com")
                .password("1234")
                .name("박병길")
                .phone("0101231234")
                .createDateTime(LocalDateTime.now())
                .createDateTime(LocalDateTime.now())
                .build();
    }
}
