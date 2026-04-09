package com.instaqueenback.instaqueen.dto.response;

import com.instaqueenback.instaqueen.entity.OrderItem;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private UUID id;
    private UUID productId;
    private String productName;
    private String productImageUrl;
    private int quantity;
    private BigDecimal unitPrice;

    public static OrderItemResponse from(OrderItem oi) {
        return OrderItemResponse.builder()
                .id(oi.getId()).productId(oi.getProduct().getId())
                .productName(oi.getProduct().getName())
                .productImageUrl(oi.getProduct().getImageUrl())
                .quantity(oi.getQuantity()).unitPrice(oi.getUnitPrice())
                .build();
    }
}
