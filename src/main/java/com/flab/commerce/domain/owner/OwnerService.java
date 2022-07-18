package com.flab.commerce.domain.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerMapper ownerMapper;

    public boolean register(OwnerRegisterDto registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getPasswordConfirm())) {
            throw new RuntimeException("비밀번호와 확인용 비밀번호가 일치하지 않습니다");
        }

        if (ownerMapper.findByEmail(registerRequest.getEmail()) != null) {
            throw new RuntimeException("이미 존재하는 이메일입니다");
        }

        int countInsertRow = ownerMapper.register(registerRequest.toOwner());
        return countInsertRow == 1;
    }
}
