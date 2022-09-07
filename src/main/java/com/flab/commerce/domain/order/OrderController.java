package com.flab.commerce.domain.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flab.commerce.security.user.GeneralUserDetails;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping("/orders")
  public ResponseEntity<Void> save(@Valid @RequestBody OrderSaveDto saveDto,
      @AuthenticationPrincipal GeneralUserDetails generalUserDetails)
      throws JsonProcessingException {
    orderService.save(generalUserDetails.getUser().getId(), saveDto);
    return ResponseEntity.ok().build();
  }
}
