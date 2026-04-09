package com.instaqueenback.instaqueen.dto.response;

import com.instaqueenback.instaqueen.entity.OfferProduct;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferProductResponse {
    private UUID productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal productPrice;
    private int quantity;

    public static OfferProductResponse from(OfferProduct op) {
        return OfferProductResponse.builder()
                .productId(op.getProduct().getId())
                .productName(op.getProduct().getName())
                .productImageUrl(op.getProduct().getImageUrl())
                .productPrice(op.getProduct().getPrice())
                .quantity(op.getQuantity())
                .build();
    }
}
