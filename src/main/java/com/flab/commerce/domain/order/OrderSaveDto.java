package com.flab.commerce.domain.order;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSaveDto {

  @NotNull
  private Long menuId;

  @NotNull
  private Long userId;

  private int amount;

  @NotBlank
  private String address;

  @NotBlank
  private String addressDetail;

  @NotBlank
  @Length(min = 5, max = 5)
  private String zipCode;

  @NotBlank
  @Length(min = 11, max = 12)
  private String phone;
}
