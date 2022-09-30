package com.flab.commerce.domain.option;

import java.math.BigInteger;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionRegisterDto {

  @NotBlank
  @Length(min = 2, max = 20)
  private String name;

  @Min(0)
  @Max(10_000_000)
  private BigInteger price;
}
