package com.flab.commerce.domain.owner;

import com.flab.commerce.exception.BadInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerMapper ownerMapper;

    public boolean register(OwnerRegisterDto registerRequest) {
        if (ownerMapper.findByEmail(registerRequest.getEmail()) != null) {
            throw new BadInputException("이미 존재하는 이메일입니다");
        }

        int countInsertRow = ownerMapper.register(registerRequest.toOwner());
        return countInsertRow == 1;
    }
}
