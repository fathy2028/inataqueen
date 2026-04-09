package com.instaqueenback.instaqueen.dto.response;

import com.instaqueenback.instaqueen.entity.Offer;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferResponse {
    private UUID id;
    private String title;
    private String description;
    private String bannerImageUrl;
    private int discountPercentage;
    private BigDecimal originalTotal;
    private BigDecimal discountedTotal;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
    private List<OfferProductResponse> products;

    public static OfferResponse from(Offer o) {
        return OfferResponse.builder()
                .id(o.getId()).title(o.getTitle()).description(o.getDescription())
                .bannerImageUrl(o.getBannerImageUrl()).discountPercentage(o.getDiscountPercentage())
                .originalTotal(o.getOriginalTotal()).discountedTotal(o.getDiscountedTotal())
                .startDate(o.getStartDate()).endDate(o.getEndDate()).isActive(o.isActive())
                .products(o.getOfferProducts() != null ? o.getOfferProducts().stream().map(OfferProductResponse::from).toList() : List.of())
                .build();
    }
}
