package com.instaqueenback.instaqueen.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {
    private String label;
    @NotBlank
    private String street;
    @NotBlank
    private String city;
    private String state;
    private String zipCode;
    private Double latitude;
    private Double longitude;
    private boolean isDefault;
}
