package com.instaqueenback.instaqueen.dto.response;

import com.instaqueenback.instaqueen.entity.User;
import com.instaqueenback.instaqueen.enums.Role;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String fullName;
    private String phone;
    private Role role;
    private String profileImageUrl;
    private boolean bioAuthEnabled;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .role(user.getRole())
                .profileImageUrl(user.getProfileImageUrl())
                .bioAuthEnabled(user.isBioAuthEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
