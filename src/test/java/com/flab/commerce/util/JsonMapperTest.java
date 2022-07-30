package com.flab.commerce.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class JsonMapperTest {

  @Test
  void 객체를_JSON으로_파싱한다() {
    JsonMapper<List<String>> mapper = new JsonMapper(new ObjectMapper());
    String json = mapper.parse(Arrays.asList("첫번째", "두번째", "세번째"));
    assertThat(json).isEqualTo("[\"첫번째\",\"두번째\",\"세번째\"]");
  }
}
