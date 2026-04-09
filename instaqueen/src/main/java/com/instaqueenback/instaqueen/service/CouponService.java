package com.instaqueenback.instaqueen.service;

import com.instaqueenback.instaqueen.dto.request.CouponRequest;
import com.instaqueenback.instaqueen.dto.request.CouponValidateRequest;
import com.instaqueenback.instaqueen.dto.response.CouponResponse;
import com.instaqueenback.instaqueen.dto.response.CouponValidateResponse;
import com.instaqueenback.instaqueen.entity.Coupon;
import com.instaqueenback.instaqueen.exception.ResourceNotFoundException;
import com.instaqueenback.instaqueen.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream().map(CouponResponse::from).toList();
    }

    public CouponResponse createCoupon(CouponRequest request) {
        Coupon coupon = Coupon.builder()
                .code(request.getCode().toUpperCase())
                .description(request.getDescription())
                .discountPercentage(request.getDiscountPercentage())
                .maxDiscount(request.getMaxDiscount())
                .minOrderAmount(request.getMinOrderAmount())
                .expiryDate(request.getExpiryDate())
                .usageLimit(request.getUsageLimit())
                .build();
        return CouponResponse.from(couponRepository.save(coupon));
    }

    public CouponResponse updateCoupon(UUID id, CouponRequest request) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        coupon.setCode(request.getCode().toUpperCase());
        coupon.setDescription(request.getDescription());
        coupon.setDiscountPercentage(request.getDiscountPercentage());
        coupon.setMaxDiscount(request.getMaxDiscount());
        coupon.setMinOrderAmount(request.getMinOrderAmount());
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setUsageLimit(request.getUsageLimit());
        return CouponResponse.from(couponRepository.save(coupon));
    }

    public void deleteCoupon(UUID id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        couponRepository.delete(coupon);
    }

    public CouponValidateResponse validateCoupon(CouponValidateRequest request) {
        Coupon coupon = couponRepository.findByCode(request.getCode().toUpperCase()).orElse(null);
        if (coupon == null) {
            return CouponValidateResponse.builder().valid(false).message("Coupon not found").build();
        }
        if (!coupon.isActive()) {
            return CouponValidateResponse.builder().valid(false).message("Coupon is inactive").build();
        }
        if (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            return CouponValidateResponse.builder().valid(false).message("Coupon has expired").build();
        }
        if (coupon.getUsedCount() >= coupon.getUsageLimit()) {
            return CouponValidateResponse.builder().valid(false).message("Coupon usage limit reached").build();
        }
        if (coupon.getMinOrderAmount() != null && request.getCartTotal().compareTo(coupon.getMinOrderAmount()) < 0) {
            return CouponValidateResponse.builder().valid(false)
                    .message("Minimum order amount is " + coupon.getMinOrderAmount()).build();
        }
        BigDecimal discount = request.getCartTotal()
                .multiply(BigDecimal.valueOf(coupon.getDiscountPercentage()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
            discount = coupon.getMaxDiscount();
        }
        return CouponValidateResponse.builder().valid(true).discountAmount(discount)
                .message("Coupon applied! You save " + discount).build();
    }

    public Coupon applyCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        return couponRepository.save(coupon);
    }
}
