package com.instaqueenback.instaqueen.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponValidateResponse {
    private boolean valid;
    private BigDecimal discountAmount;
    private String message;
}
