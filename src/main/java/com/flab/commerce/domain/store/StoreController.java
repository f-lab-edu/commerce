package com.flab.commerce.domain.store;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<Void> register(
            @Valid @RequestBody StoreRegisterDto registerRequest,
            HttpSession session
    ) {
        Long ownerId = (Long) session.getAttribute("OWNER");
        storeService.register(registerRequest, ownerId);
        return ResponseEntity.noContent().build();
    }
}
