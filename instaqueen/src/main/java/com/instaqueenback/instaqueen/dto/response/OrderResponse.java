package com.instaqueenback.instaqueen.dto.response;

import com.instaqueenback.instaqueen.entity.Order;
import com.instaqueenback.instaqueen.enums.OrderStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private UUID id;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private OrderStatus status;
    private String notes;
    private CouponResponse coupon;
    private OfferResponse offer;
    private List<OrderItemResponse> items;
    private AddressResponse address;
    private String customerName;
    private String customerEmail;
    private LocalDateTime createdAt;

    public static OrderResponse from(Order o) {
        return OrderResponse.builder()
                .id(o.getId()).subtotal(o.getSubtotal()).discountAmount(o.getDiscountAmount())
                .finalAmount(o.getFinalAmount()).status(o.getStatus()).notes(o.getNotes())
                .coupon(o.getCoupon() != null ? CouponResponse.from(o.getCoupon()) : null)
                .offer(o.getOffer() != null ? OfferResponse.from(o.getOffer()) : null)
                .items(o.getOrderItems().stream().map(OrderItemResponse::from).toList())
                .address(AddressResponse.from(o.getAddress()))
                .customerName(o.getUser().getFullName())
                .customerEmail(o.getUser().getEmail())
                .createdAt(o.getCreatedAt())
                .build();
    }
}
