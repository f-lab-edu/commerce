package com.flab.commerce.domain.optiongroup;

import com.flab.commerce.domain.option.OptionResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OptionGroupDetailDto {

  private Long id;

  private String name;

  private List<OptionResponseDto> options;
}
