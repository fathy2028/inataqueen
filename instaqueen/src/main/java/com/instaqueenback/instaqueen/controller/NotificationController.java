package com.instaqueenback.instaqueen.controller;

import com.instaqueenback.instaqueen.dto.request.SendNotificationRequest;
import com.instaqueenback.instaqueen.dto.response.*;
import com.instaqueenback.instaqueen.service.NotificationService;
import com.instaqueenback.instaqueen.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getAll(
            @AuthenticationPrincipal UserDetails userDetails, @PageableDefault(size = 20) Pageable pageable) {
        var user = userService.findByEmail(userDetails.getUsername());
        var page = notificationService.getUserNotifications(user.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.from(page, n -> n)));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markRead(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID id) {
        var user = userService.findByEmail(userDetails.getUsername());
        notificationService.markAsRead(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.ok("Marked as read", null));
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllRead(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByEmail(userDetails.getUsername());
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok(ApiResponse.ok("All marked as read", null));
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Void>> send(@Valid @RequestBody SendNotificationRequest request) {
        notificationService.sendNotification(request);
        return ResponseEntity.ok(ApiResponse.ok("Notification sent", null));
    }
}
