package com.flab.commerce.domain.optiongroup;

import com.flab.commerce.domain.store.StoreService;
import com.flab.commerce.security.owner.OwnerDetails;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/option-groups")
public class OptionGroupController {

  private final OptionGroupService optionGroupService;

  private final StoreService storeService;

  @PostMapping
  public ResponseEntity<Void> registerOptionGroup(@PathVariable Long storeId,
      @AuthenticationPrincipal OwnerDetails ownerDetails,
      @Valid @RequestBody OptionGroupRegisterDto optionGroupRegisterDto) {

    storeService.validateOwnerStore(ownerDetails.getOwner().getId(), storeId);

    OptionGroup optionGroup = OptionGroupObjectMapper.INSTANCE.toEntity(optionGroupRegisterDto, storeId);
    optionGroupService.registerOptionGroup(optionGroup);

    return ResponseEntity.ok().build();
  }
}
