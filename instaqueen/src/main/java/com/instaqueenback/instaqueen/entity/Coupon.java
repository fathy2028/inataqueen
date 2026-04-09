package com.instaqueenback.instaqueen.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String code;

    private String description;

    @Column(nullable = false)
    private int discountPercentage;

    private BigDecimal maxDiscount;
    private BigDecimal minOrderAmount;
    private LocalDateTime expiryDate;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private int usageLimit = 100;

    @Builder.Default
    private int usedCount = 0;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
