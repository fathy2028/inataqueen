package com.instaqueenback.instaqueen.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @Email @NotBlank
    private String email;
    @NotBlank @Size(min = 6)
    private String password;
    @NotBlank
    private String fullName;
    private String phone;
}
