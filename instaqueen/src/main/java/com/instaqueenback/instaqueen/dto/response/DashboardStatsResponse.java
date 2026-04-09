package com.instaqueenback.instaqueen.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {
    private long totalCustomers;
    private long totalProducts;
    private long totalOffers;
    private long totalCoupons;
    private BigDecimal todayEarnings;
    private BigDecimal monthEarnings;
    private List<TopProductResponse> topSellingProducts;
    private List<TopOfferResponse> topOffers;
    private List<ProductResponse> lowStockProducts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopProductResponse {
        private String productId;
        private String productName;
        private long totalSold;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopOfferResponse {
        private String offerId;
        private String offerTitle;
        private long timesTaken;
    }
}
