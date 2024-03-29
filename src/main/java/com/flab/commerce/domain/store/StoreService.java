package com.flab.commerce.domain.store;

import com.flab.commerce.exception.BadInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreMapper storeMapper;

  public boolean register(StoreRegisterDto registerRequest, Long ownerId) {
    int countInsertRow = storeMapper.register(registerRequest.toStore(ownerId));
    return countInsertRow == 1;
  }

  public void validateStoreExistence(Long storeId) {
    if (!storeMapper.idExists(storeId)) {
      throw new BadInputException("해당 가게는 존재하지 않습니다.");
    }
  }

  public void validateOwnerStore(Long ownerId, Long storeId) {
    validateStoreExistence(storeId);

    if (!storeMapper.idAndOwnerIdExists(storeId, ownerId)) {
      throw new AccessDeniedException("가게의 사장님이 아닙니다.");
    }
  }
}
