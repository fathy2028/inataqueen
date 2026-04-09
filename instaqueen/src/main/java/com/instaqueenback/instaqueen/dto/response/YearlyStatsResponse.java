package com.instaqueenback.instaqueen.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YearlyStatsResponse {
    private int year;
    private List<MonthlyEarningResponse> monthlyEarnings;
    private BigDecimal yearTotal;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MonthlyEarningResponse {
        private int month;
        private String monthName;
        private BigDecimal totalEarnings;
    }
}
