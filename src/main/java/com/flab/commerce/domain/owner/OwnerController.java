package com.flab.commerce.domain.owner;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OwnerController {

  private final OwnerService ownerService;

  @PostMapping("/owners")
  public ResponseEntity<Void> register(@Valid @RequestBody OwnerRegisterDto registerRequest) {
    ownerService.register(registerRequest);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
