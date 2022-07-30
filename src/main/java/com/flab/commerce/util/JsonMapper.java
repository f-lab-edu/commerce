package com.flab.commerce.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.exception.BadParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonMapper<T> {

  private final ObjectMapper mapper;

  public String parse(T object) {
    String json;
    try {
      json = mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new BadParseException(e.getMessage());
    }
    return json;
  }
}
