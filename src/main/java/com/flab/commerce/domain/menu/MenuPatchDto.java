package com.flab.commerce.domain.menu;

import java.math.BigInteger;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MenuPatchDto {

  @Length(min = 2, max = 20)
  private String name;

  @Min(0)
  @Max(10_000_000)
  private BigInteger price;

  @Length(min = 40, max = 41)
  private String image;
}
