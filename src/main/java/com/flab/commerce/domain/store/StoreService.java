package com.flab.commerce.domain.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreMapper storeMapper;

    public boolean register(StoreRegisterDto registerRequest, Long ownerId) {
        int countInsertRow = storeMapper.register(registerRequest.toStore(ownerId));
        return countInsertRow == 1;
    }
}
