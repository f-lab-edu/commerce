package com.flab.commerce.domain.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/users")
    public ResponseEntity<Void> register(@Valid @RequestBody OwnerRegisterDto registerRequest) {
        ownerService.register(registerRequest);
        return ResponseEntity.ok().build();
    }
}
