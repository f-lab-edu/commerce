package com.flab.commerce.domain.optiongroup;

import com.flab.commerce.domain.store.StoreService;
import com.flab.commerce.security.owner.OwnerDetails;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    OptionGroup optionGroup = OptionGroupObjectMapper.INSTANCE.toEntity(optionGroupRegisterDto,
        storeId);
    optionGroupService.registerOptionGroup(optionGroup);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping
  public ResponseEntity<List<OptionGroupReadDto>> getOptionGroups(@PathVariable Long storeId,
      @AuthenticationPrincipal OwnerDetails ownerDetails) {

    storeService.validateOwnerStore(ownerDetails.getOwner().getId(), storeId);

    List<OptionGroup> optionGroups = optionGroupService.getOptionGroups(storeId);

    List<OptionGroupReadDto> optionGroupReadDtos = OptionGroupObjectMapper.INSTANCE.toDto(
        optionGroups);

    return ResponseEntity.ok().body(optionGroupReadDtos);
  }

  @DeleteMapping("/{optionGroupId}")
  public ResponseEntity<Void> deleteOptionGroup(@PathVariable Long storeId,
      @PathVariable Long optionGroupId, @AuthenticationPrincipal OwnerDetails ownerDetails) {

    storeService.validateOwnerStore(ownerDetails.getOwner().getId(), storeId);

    optionGroupService.validateOptionGroupStore(optionGroupId, storeId);

    optionGroupService.deleteOptionGroup(optionGroupId);

    return ResponseEntity.ok().build();
  }

  @PutMapping("/{optionGroupId}")
  public ResponseEntity<Void> updateOptionGroup(@PathVariable Long storeId,
      @PathVariable Long optionGroupId, @AuthenticationPrincipal OwnerDetails ownerDetails,
      @Valid @RequestBody OptionGroupUpdateDto optionGroupUpdateDto) {

    storeService.validateOwnerStore(ownerDetails.getOwner().getId(), storeId);

    optionGroupService.validateOptionGroupStore(optionGroupId, storeId);

    OptionGroup optionGroup = OptionGroupObjectMapper.INSTANCE.toEntity(optionGroupUpdateDto, storeId);
    optionGroupService.updateOptionGroup(optionGroup);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/{optionGroupId}")
  public ResponseEntity<OptionGroupAndOptionsResponseDto> getOptionGroup(@PathVariable Long storeId,
      @PathVariable Long optionGroupId, @AuthenticationPrincipal OwnerDetails ownerDetails) {

    storeService.validateOwnerStore(ownerDetails.getOwner().getId(), storeId);

    optionGroupService.validateOptionGroupStore(optionGroupId, storeId);

    OptionGroup optionGroup = optionGroupService.getOptionGroupAndOptions(optionGroupId);

    OptionGroupAndOptionsResponseDto dto = OptionGroupObjectMapper.INSTANCE.toDto(optionGroup);

    return ResponseEntity.ok(dto);
  }
}
