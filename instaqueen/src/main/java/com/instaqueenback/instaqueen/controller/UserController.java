package com.instaqueenback.instaqueen.controller;

import com.instaqueenback.instaqueen.dto.request.UpdateProfileRequest;
import com.instaqueenback.instaqueen.dto.response.*;
import com.instaqueenback.instaqueen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getProfileByEmail(userDetails.getUsername())));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateProfileRequest request) {
        var user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(userService.updateProfile(user.getId(), request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        var page = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.from(page, u -> u)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable UUID id, @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(userService.updateUser(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok("User deleted", null));
    }
}
