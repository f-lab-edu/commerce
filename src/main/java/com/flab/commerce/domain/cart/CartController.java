package com.flab.commerce.domain.cart;

import com.flab.commerce.security.user.GeneralUserDetails;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  private final CartValidator cartValidator;

  @InitBinder("cartRegisterDto")
  public void initRegisterDto(WebDataBinder webDataBinder) {
    webDataBinder.addValidators(cartValidator);
  }

  @PostMapping
  public ResponseEntity<Void> addMenu(
      @AuthenticationPrincipal GeneralUserDetails generalUserDetails,
      @Valid @RequestBody CartRegisterDto cartRegisterDto) {

    Long userId = generalUserDetails.getUser().getId();
    Cart cart = CartObjectMapper.INSTANCE.dtoToCart(cartRegisterDto, userId);
    cartService.addMenu(cart);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping
  public ResponseEntity<Void> updateAmount(
      @AuthenticationPrincipal GeneralUserDetails generalUserDetails,
      @Valid @RequestBody CartUpdateDto cartUpdateDto) {

    Long userId = generalUserDetails.getUser().getId();
    Cart cart = CartObjectMapper.INSTANCE.dtoToCart(cartUpdateDto, userId);
    cartService.updateAmount(cart);

    return ResponseEntity.ok().build();
  }
}
