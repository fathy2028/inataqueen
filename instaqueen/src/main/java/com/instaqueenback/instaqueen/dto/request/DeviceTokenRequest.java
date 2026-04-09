package com.instaqueenback.instaqueen.dto.request;

import com.instaqueenback.instaqueen.enums.Platform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTokenRequest {
    @NotBlank
    private String token;
    @NotNull
    private Platform platform;
}
