package com.instaqueenback.instaqueen.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    private List<OrderItemRequest> items;
    private UUID offerId;
    @NotNull
    private UUID addressId;
    private String couponCode;
    private String notes;
}
