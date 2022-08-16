package com.flab.commerce.domain.option;

import com.flab.commerce.exception.BadInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptionService {

  private final OptionMapper optionMapper;

  public void registerOption(Option option) {
    optionMapper.save(option);
  }

  public void updateOption(Option option){
    if (optionMapper.update(option) == 0){
      throw new BadInputException("옵션을 찾을 수 없습니다.");
    }
  }

  public void validate(Long optionId, Long optionGroupId) {
    if (!optionMapper.idExists(optionId)) {
      throw new BadInputException("옵션이 존재하지 않습니다.");
    }
    if (!optionMapper.idAndOptionGroupIdExists(optionId, optionGroupId)) {
      throw new BadInputException("옵션그룹의 옵션이 아닙니다.");
    }
  }

  public void deleteOption(Long optionId) {
    if (optionMapper.delete(optionId) == 0) {
      throw new BadInputException("옵션을 찾을 수 없습니다.");
    }
  }
}
