package com.flab.commerce.domain.store;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.StoreRegisterDto.StoreRegisterDtoBuilder;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.security.WithMockDetails;
import com.flab.commerce.util.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StoreController.class)
@ComponentScan(basePackages = "com.flab.commerce.security")
@WithMockDetails(role = Constants.ROLE_OWNER)
class StoreControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private StoreService storeService;

  @MockBean
  private OwnerMapper ownerMapper;

  @MockBean
  private UserMapper userMapper;

  @Test
  void 식당을_등록한다() throws Exception {
    String body = mapper.writeValueAsString(createRegisterRequest().build());

    mvc.perform(post("/stores")
            .session(new MockHttpSession())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void 식당을_등록_이름_확인() throws Exception {
    String body = mapper.writeValueAsString(createRegisterRequest().name("").build());

    mvc.perform(post("/stores")
            .session(new MockHttpSession())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  private StoreRegisterDtoBuilder createRegisterRequest() {
    return StoreRegisterDto.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집");
  }
}
