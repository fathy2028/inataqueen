package com.instaqueenback.instaqueen.dto.request;

import com.instaqueenback.instaqueen.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {
    @NotNull
    private OrderStatus status;
}
