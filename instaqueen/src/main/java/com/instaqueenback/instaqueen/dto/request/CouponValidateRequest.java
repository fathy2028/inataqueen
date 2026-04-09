package com.instaqueenback.instaqueen.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponValidateRequest {
    @NotBlank
    private String code;
    @NotNull @DecimalMin("0.01")
    private BigDecimal cartTotal;
}
