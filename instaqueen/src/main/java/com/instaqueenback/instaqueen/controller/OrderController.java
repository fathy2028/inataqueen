package com.instaqueenback.instaqueen.controller;

import com.instaqueenback.instaqueen.dto.request.*;
import com.instaqueenback.instaqueen.dto.response.*;
import com.instaqueenback.instaqueen.enums.OrderStatus;
import com.instaqueenback.instaqueen.service.OrderService;
import com.instaqueenback.instaqueen.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateOrderRequest request) {
        var user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Order created", orderService.createOrder(user.getId(), request)));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> myOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 20) Pageable pageable) {
        var user = userService.findByEmail(userDetails.getUsername());
        var page = orderService.getMyOrders(user.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.from(page, o -> o)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable java.util.UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getOrderDetail(id)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @PathVariable java.util.UUID id, @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Order status updated", orderService.updateOrderStatus(id, request.getStatus())));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getAll(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) OrderStatus status) {
        var page = orderService.getAllOrders(pageable, status);
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.from(page, o -> o)));
    }
}
