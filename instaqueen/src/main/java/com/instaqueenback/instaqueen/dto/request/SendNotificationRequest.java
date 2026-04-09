package com.instaqueenback.instaqueen.dto.request;

import com.instaqueenback.instaqueen.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendNotificationRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String body;
    private NotificationType type;
    private List<UUID> targetUserIds;
}
