package com.flab.commerce.domain.order;

import java.util.List;
import javax.validation.Valid;
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
  @Valid
  @NotNull
  private List<OrderMenuDto> menus;

  @NotBlank
  @Length(min = 5, max = 5)
  private String zipCode;

  @NotBlank
  private String address;

  @NotBlank
  private String addressDetail;

  @NotBlank
  @Length(min = 9)
  private String phone;
}
