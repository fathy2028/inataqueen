package com.instaqueenback.instaqueen.controller;

import com.instaqueenback.instaqueen.dto.request.*;
import com.instaqueenback.instaqueen.dto.response.*;
import com.instaqueenback.instaqueen.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CouponResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(couponService.getAllCoupons()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CouponResponse>> create(@Valid @RequestBody CouponRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Coupon created", couponService.createCoupon(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CouponResponse>> update(@PathVariable UUID id, @Valid @RequestBody CouponRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Coupon updated", couponService.updateCoupon(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok(ApiResponse.ok("Coupon deleted", null));
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<CouponValidateResponse>> validate(@Valid @RequestBody CouponValidateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(couponService.validateCoupon(request)));
    }
}
