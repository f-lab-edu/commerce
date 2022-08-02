package com.flab.commerce.domain.owner;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class PrincipalOwnerDto implements Serializable {

  private final String id;

  private final String email;

  private final String name;

  private final String phone;

  private final LocalDateTime createDateTime;

  private final LocalDateTime modifyDateTime;
}