package com.instaqueenback.instaqueen.controller;

import com.instaqueenback.instaqueen.dto.request.AddressRequest;
import com.instaqueenback.instaqueen.dto.response.*;
import com.instaqueenback.instaqueen.service.AddressService;
import com.instaqueenback.instaqueen.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(addressService.getUserAddresses(user.getId())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> create(
            @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody AddressRequest request) {
        var user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Address added", addressService.addAddress(user.getId(), request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> update(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID id, @Valid @RequestBody AddressRequest request) {
        var user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(addressService.updateAddress(user.getId(), id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID id) {
        var user = userService.findByEmail(userDetails.getUsername());
        addressService.deleteAddress(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.ok("Address deleted", null));
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefault(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID id) {
        var user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(addressService.setDefault(user.getId(), id)));
    }
}
