package com.instaqueenback.instaqueen.dto.response;

import com.instaqueenback.instaqueen.entity.Coupon;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponse {
    private UUID id;
    private String code;
    private String description;
    private int discountPercentage;
    private BigDecimal maxDiscount;
    private BigDecimal minOrderAmount;
    private LocalDateTime expiryDate;
    private boolean isActive;
    private int usageLimit;
    private int usedCount;

    public static CouponResponse from(Coupon c) {
        return CouponResponse.builder()
                .id(c.getId()).code(c.getCode()).description(c.getDescription())
                .discountPercentage(c.getDiscountPercentage()).maxDiscount(c.getMaxDiscount())
                .minOrderAmount(c.getMinOrderAmount()).expiryDate(c.getExpiryDate())
                .isActive(c.isActive()).usageLimit(c.getUsageLimit()).usedCount(c.getUsedCount())
                .build();
    }
}
