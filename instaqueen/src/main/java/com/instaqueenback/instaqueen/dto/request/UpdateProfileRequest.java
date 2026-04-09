package com.instaqueenback.instaqueen.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    private String fullName;
    private String phone;
    private String profileImageUrl;
}
