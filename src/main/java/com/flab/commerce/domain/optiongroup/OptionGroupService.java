package com.flab.commerce.domain.optiongroup;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptionGroupService {

  private final OptionGroupMapper optionGroupMapper;

  public void registerOptionGroup(OptionGroup optionGroup) {
    optionGroupMapper.save(optionGroup);
  }

  public List<OptionGroup> getOptionGroups(Long storeId) {
    return optionGroupMapper.findByStoreId(storeId);
  }
}
