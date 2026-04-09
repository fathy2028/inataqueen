package com.instaqueenback.instaqueen.dto.response;

import com.instaqueenback.instaqueen.entity.Notification;
import com.instaqueenback.instaqueen.enums.NotificationType;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private UUID id;
    private String title;
    private String body;
    private NotificationType type;
    private String referenceId;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId()).title(n.getTitle()).body(n.getBody())
                .type(n.getType()).referenceId(n.getReferenceId())
                .isRead(n.isRead()).createdAt(n.getCreatedAt())
                .build();
    }
}
