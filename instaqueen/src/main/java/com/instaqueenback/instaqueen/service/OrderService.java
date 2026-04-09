package com.instaqueenback.instaqueen.service;

import com.instaqueenback.instaqueen.dto.request.CreateOrderRequest;
import com.instaqueenback.instaqueen.dto.response.OrderResponse;
import com.instaqueenback.instaqueen.entity.*;
import com.instaqueenback.instaqueen.enums.OrderStatus;
import com.instaqueenback.instaqueen.exception.BadRequestException;
import com.instaqueenback.instaqueen.exception.ResourceNotFoundException;
import com.instaqueenback.instaqueen.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final CouponService couponService;

    @Transactional
    public OrderResponse createOrder(UUID userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        Order order = Order.builder().user(user).address(address).notes(request.getNotes()).build();
        BigDecimal subtotal = BigDecimal.ZERO;

        if (request.getOfferId() != null) {
            Offer offer = offerRepository.findById(request.getOfferId())
                    .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
            order.setOffer(offer);
            for (OfferProduct op : offer.getOfferProducts()) {
                Product product = op.getProduct();
                if (product.getStock() < op.getQuantity()) {
                    throw new BadRequestException("Insufficient stock for: " + product.getName());
                }
                product.setStock(product.getStock() - op.getQuantity());
                productRepository.save(product);
                order.getOrderItems().add(OrderItem.builder()
                        .order(order).product(product)
                        .quantity(op.getQuantity()).unitPrice(product.getPrice()).build());
            }
            subtotal = offer.getOriginalTotal();
            order.setSubtotal(subtotal);
            order.setDiscountAmount(offer.getOriginalTotal().subtract(offer.getDiscountedTotal()));
            order.setFinalAmount(offer.getDiscountedTotal());
        } else {
            if (request.getItems() == null || request.getItems().isEmpty()) {
                throw new BadRequestException("Order must have items or an offer");
            }
            for (var item : request.getItems()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + item.getProductId()));
                if (product.getStock() < item.getQuantity()) {
                    throw new BadRequestException("Insufficient stock for: " + product.getName());
                }
                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);
                subtotal = subtotal.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                order.getOrderItems().add(OrderItem.builder()
                        .order(order).product(product)
                        .quantity(item.getQuantity()).unitPrice(product.getPrice()).build());
            }
            order.setSubtotal(subtotal);
            order.setDiscountAmount(BigDecimal.ZERO);
            order.setFinalAmount(subtotal);
        }

        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            var validation = couponService.validateCoupon(
                    new com.instaqueenback.instaqueen.dto.request.CouponValidateRequest(
                            request.getCouponCode(), order.getFinalAmount()));
            if (validation.isValid()) {
                Coupon coupon = couponService.applyCoupon(request.getCouponCode());
                order.setCoupon(coupon);
                BigDecimal currentDiscount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;
                order.setDiscountAmount(currentDiscount.add(validation.getDiscountAmount()));
                order.setFinalAmount(order.getFinalAmount().subtract(validation.getDiscountAmount()));
            }
        }

        if (order.getFinalAmount().compareTo(BigDecimal.ZERO) < 0) {
            order.setFinalAmount(BigDecimal.ZERO);
        }

        return OrderResponse.from(orderRepository.save(order));
    }

    public Page<OrderResponse> getMyOrders(UUID userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable).map(OrderResponse::from);
    }

    public OrderResponse getOrderDetail(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return OrderResponse.from(order);
    }

    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        return OrderResponse.from(orderRepository.save(order));
    }

    public Page<OrderResponse> getAllOrders(Pageable pageable, OrderStatus status) {
        if (status != null) {
            return orderRepository.findByStatus(status, pageable).map(OrderResponse::from);
        }
        return orderRepository.findAll(pageable).map(OrderResponse::from);
    }
}
