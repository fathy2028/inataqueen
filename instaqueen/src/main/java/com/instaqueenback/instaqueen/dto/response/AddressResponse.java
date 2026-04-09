package com.instaqueenback.instaqueen.dto.response;

import com.instaqueenback.instaqueen.entity.Address;
import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
    private UUID id;
    private String label;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private Double latitude;
    private Double longitude;
    private boolean isDefault;

    public static AddressResponse from(Address a) {
        return AddressResponse.builder()
                .id(a.getId()).label(a.getLabel()).street(a.getStreet())
                .city(a.getCity()).state(a.getState()).zipCode(a.getZipCode())
                .latitude(a.getLatitude()).longitude(a.getLongitude()).isDefault(a.isDefault())
                .build();
    }
}
