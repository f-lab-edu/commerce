package com.flab.commerce.domain.option;

import com.flab.commerce.domain.optiongroup.OptionGroupService;
import com.flab.commerce.domain.store.StoreService;
import com.flab.commerce.security.owner.OwnerDetails;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/option-groups/{optionGroupId}/options")
public class OptionController {

  private final OptionService optionService;
  private final OptionGroupService optionGroupService;
  private final StoreService storeService;

  @PostMapping
  public ResponseEntity<Void> registerOption(@PathVariable Long storeId,
      @PathVariable Long optionGroupId, @Valid @RequestBody OptionRegisterDto optionRegisterDto,
      @AuthenticationPrincipal OwnerDetails ownerDetails) {

    storeService.validateOwnerStore(ownerDetails.getOwner().getId(), storeId);
    optionGroupService.validateOptionGroupStore(optionGroupId, storeId);

    Option option = OptionObjectMapper.INSTANCE.toEntity(optionRegisterDto, optionGroupId);
    optionService.registerOption(option);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}