package com.flab.commerce.domain.optiongroup;

import com.flab.commerce.exception.BadInputException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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

  public void deleteOptionGroup(Long id) {
    if (optionGroupMapper.delete(id) == 0) {
      throw new BadInputException("옵션 그룹을 찾을 수 없습니다.");
    }
  }

  public void validateOptionGroupStore(Long optionGroupId, Long storeId) {
    if (!optionGroupMapper.idExists(optionGroupId)) {
      throw new BadInputException("옵션 그룹이 존재하지 않습니다.");
    }
    if (!optionGroupMapper.idAndStoreIdExists(optionGroupId, storeId)) {
      throw new AccessDeniedException("다른 가게의 옵션 그룹입니다");
    }
  }

  public void updateOptionGroup(OptionGroup optionGroup) {
    if (optionGroupMapper.update(optionGroup) == 0) {
      throw new BadInputException("옵션 그룹을 찾을 수 없습니다.");
    }
  }

  public OptionGroup getOptionGroupAndOptions(Long id) {
    return optionGroupMapper.selectOptionGroupAndOptions(id);
  }
}
