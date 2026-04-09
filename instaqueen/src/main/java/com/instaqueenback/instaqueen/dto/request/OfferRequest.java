package com.instaqueenback.instaqueen.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferRequest {
    @NotBlank
    private String title;
    private String description;
    private String bannerImageUrl;
    @Min(1) @Max(100)
    private int discountPercentage;
    private BigDecimal originalTotal;
    private BigDecimal discountedTotal;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<OfferProductRequest> products;
}
