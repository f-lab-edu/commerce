package com.flab.commerce.domain.optiongroup;

import com.flab.commerce.domain.option.OptionResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionGroupAndOptionsResponseDto {

  private Long id;

  private String name;

  private List<OptionResponseDto> options;
}
