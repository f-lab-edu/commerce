package com.flab.commerce.domain.order;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping("/orders")
  public ResponseEntity<Void> save(@Valid @RequestBody OrderSaveDto saveDto) {
    orderService.save(saveDto);
    return ResponseEntity.ok().build();
  }
}
