package com.instaqueenback.instaqueen.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    @NotNull
    private UUID productId;
    @Min(1)
    private int quantity;
}
