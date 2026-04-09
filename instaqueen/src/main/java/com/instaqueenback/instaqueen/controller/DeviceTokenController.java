package com.instaqueenback.instaqueen.controller;

import com.instaqueenback.instaqueen.dto.request.DeviceTokenRequest;
import com.instaqueenback.instaqueen.dto.response.ApiResponse;
import com.instaqueenback.instaqueen.service.DeviceTokenService;
import com.instaqueenback.instaqueen.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/device-tokens")
@RequiredArgsConstructor
public class DeviceTokenController {

    private final DeviceTokenService deviceTokenService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> register(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody DeviceTokenRequest request) {
        var user = userService.findByEmail(userDetails.getUsername());
        deviceTokenService.registerToken(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Token registered", null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> remove(@RequestParam String token) {
        deviceTokenService.removeToken(token);
        return ResponseEntity.ok(ApiResponse.ok("Token removed", null));
    }
}
