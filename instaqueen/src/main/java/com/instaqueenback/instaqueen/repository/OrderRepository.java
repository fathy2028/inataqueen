package com.instaqueenback.instaqueen.repository;

import com.instaqueenback.instaqueen.entity.Order;
import com.instaqueenback.instaqueen.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findByUserId(UUID userId, Pageable pageable);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT COALESCE(SUM(o.finalAmount), 0) FROM Order o WHERE o.status = :status AND o.createdAt BETWEEN :start AND :end")
    BigDecimal sumFinalAmountByStatusAndCreatedAtBetween(@Param("status") OrderStatus status, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(o.finalAmount), 0) FROM Order o WHERE o.status = :status AND EXTRACT(YEAR FROM o.createdAt) = :year AND EXTRACT(MONTH FROM o.createdAt) = :month")
    BigDecimal sumEarningsByMonth(@Param("status") OrderStatus status, @Param("year") int year, @Param("month") int month);
}
