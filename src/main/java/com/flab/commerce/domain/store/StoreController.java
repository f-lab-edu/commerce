package com.flab.commerce.domain.store;

import com.flab.commerce.security.owner.OwnerDetails;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<Void> register(
            @Valid @RequestBody StoreRegisterDto registerRequest,
            @AuthenticationPrincipal OwnerDetails ownerDetails
    ) {
        Long ownerId = ownerDetails.getOwner().getId();
        storeService.register(registerRequest, ownerId);
        return ResponseEntity.ok().build();
    }
}
