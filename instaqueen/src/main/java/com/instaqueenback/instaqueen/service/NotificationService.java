package com.instaqueenback.instaqueen.service;

import com.instaqueenback.instaqueen.dto.request.SendNotificationRequest;
import com.instaqueenback.instaqueen.dto.response.NotificationResponse;
import com.instaqueenback.instaqueen.entity.Notification;
import com.instaqueenback.instaqueen.entity.Offer;
import com.instaqueenback.instaqueen.entity.User;
import com.instaqueenback.instaqueen.enums.NotificationType;
import com.instaqueenback.instaqueen.exception.ResourceNotFoundException;
import com.instaqueenback.instaqueen.repository.NotificationRepository;
import com.instaqueenback.instaqueen.repository.UserRepository;
import com.instaqueenback.instaqueen.websocket.NotificationWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationWebSocketHandler webSocketHandler;

    public Page<NotificationResponse> getUserNotifications(UUID userId, Pageable pageable) {
        return notificationRepository.findUserNotifications(userId, pageable).map(NotificationResponse::from);
    }

    public void markAsRead(UUID userId, UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        notificationRepository.markAllAsRead(userId);
    }

    public void sendNotification(SendNotificationRequest request) {
        if (request.getTargetUserIds() != null && !request.getTargetUserIds().isEmpty()) {
            for (UUID userId : request.getTargetUserIds()) {
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    saveNotification(user, request.getTitle(), request.getBody(), request.getType(), null);
                }
            }
        } else {
            saveNotification(null, request.getTitle(), request.getBody(), request.getType(), null);
        }

        // Push to all connected clients via WebSocket
        webSocketHandler.broadcast(Map.of(
                "type", request.getType() != null ? request.getType().name() : "GENERAL",
                "title", request.getTitle(),
                "body", request.getBody()
        ));
    }

    public void sendOfferNotification(Offer offer) {
        String title = "New Offer: " + offer.getTitle();
        String body = offer.getDescription() + " - " + offer.getDiscountPercentage() + "% OFF!";
        saveNotification(null, title, body, NotificationType.OFFER, offer.getId().toString());

        // Push to all connected clients via WebSocket
        webSocketHandler.broadcast(Map.of(
                "type", "OFFER",
                "title", title,
                "body", body,
                "referenceId", offer.getId().toString()
        ));
    }

    private void saveNotification(User user, String title, String body, NotificationType type, String referenceId) {
        Notification notification = Notification.builder()
                .user(user).title(title).body(body).type(type).referenceId(referenceId).build();
        notificationRepository.save(notification);
    }
}
