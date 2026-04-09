package com.instaqueenback.instaqueen.service;

import com.instaqueenback.instaqueen.dto.response.*;
import com.instaqueenback.instaqueen.enums.OrderStatus;
import com.instaqueenback.instaqueen.enums.Role;
import com.instaqueenback.instaqueen.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OfferRepository offerRepository;
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public DashboardStatsResponse getDashboardStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();

        BigDecimal todayEarnings = orderRepository.sumFinalAmountByStatusAndCreatedAtBetween(
                OrderStatus.DELIVERED, startOfDay, endOfDay);
        BigDecimal monthEarnings = orderRepository.sumFinalAmountByStatusAndCreatedAtBetween(
                OrderStatus.DELIVERED, startOfMonth, endOfDay);

        List<Object[]> topProducts = orderItemRepository.findTopSellingProducts(PageRequest.of(0, 10));
        List<DashboardStatsResponse.TopProductResponse> topSellingProducts = topProducts.stream()
                .map(row -> DashboardStatsResponse.TopProductResponse.builder()
                        .productId(row[0].toString())
                        .productName((String) row[1])
                        .totalSold(((Number) row[2]).longValue())
                        .build())
                .toList();

        List<ProductResponse> lowStock = productRepository.findByStockLessThanEqualAndIsActiveTrue(2)
                .stream().map(ProductResponse::from).toList();

        return DashboardStatsResponse.builder()
                .totalCustomers(userRepository.countByRole(Role.CUSTOMER))
                .totalProducts(productRepository.countByIsActiveTrue())
                .totalOffers(offerRepository.countByIsActiveTrue())
                .totalCoupons(couponRepository.count())
                .todayEarnings(todayEarnings)
                .monthEarnings(monthEarnings)
                .topSellingProducts(topSellingProducts)
                .topOffers(List.of())
                .lowStockProducts(lowStock)
                .build();
    }

    public YearlyStatsResponse getYearlyStats(int year) {
        List<YearlyStatsResponse.MonthlyEarningResponse> monthly = new ArrayList<>();
        BigDecimal yearTotal = BigDecimal.ZERO;
        for (int m = 1; m <= 12; m++) {
            BigDecimal earnings = orderRepository.sumEarningsByMonth(OrderStatus.DELIVERED, year, m);
            yearTotal = yearTotal.add(earnings);
            monthly.add(YearlyStatsResponse.MonthlyEarningResponse.builder()
                    .month(m)
                    .monthName(Month.of(m).getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                    .totalEarnings(earnings)
                    .build());
        }
        return YearlyStatsResponse.builder().year(year).monthlyEarnings(monthly).yearTotal(yearTotal).build();
    }
}
