package com.instaqueenback.instaqueen.controller;

import com.instaqueenback.instaqueen.dto.response.*;
import com.instaqueenback.instaqueen.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> dashboard() {
        return ResponseEntity.ok(ApiResponse.ok(statisticsService.getDashboardStats()));
    }

    @GetMapping("/yearly")
    public ResponseEntity<ApiResponse<YearlyStatsResponse>> yearly(@RequestParam(defaultValue = "0") int year) {
        if (year == 0) year = LocalDate.now().getYear();
        return ResponseEntity.ok(ApiResponse.ok(statisticsService.getYearlyStats(year)));
    }
}
