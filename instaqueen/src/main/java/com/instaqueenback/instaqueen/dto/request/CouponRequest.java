package com.instaqueenback.instaqueen.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponRequest {
    @NotBlank
    private String code;
    private String description;
    @Min(1) @Max(100)
    private int discountPercentage;
    private BigDecimal maxDiscount;
    private BigDecimal minOrderAmount;
    private LocalDateTime expiryDate;
    @Min(1)
    private int usageLimit;
}
