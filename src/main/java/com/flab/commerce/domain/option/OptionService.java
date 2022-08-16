package com.flab.commerce.domain.option;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptionService {

  private final OptionMapper optionMapper;

  public void registerOption(Option option) {
    optionMapper.save(option);
  }
}
