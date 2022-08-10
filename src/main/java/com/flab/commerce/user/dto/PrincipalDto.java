package com.flab.commerce.user.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class PrincipalDto implements Serializable {

  private final String id;
  private final String email;
  private final String name;
  private final String phone;
  private final String zipcode;
  private final String address;
  private final ZonedDateTime createDateTime;
  private final ZonedDateTime modifyDateTime;
}