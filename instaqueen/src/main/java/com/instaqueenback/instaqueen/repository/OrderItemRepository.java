package com.instaqueenback.instaqueen.repository;

import com.instaqueenback.instaqueen.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    @Query("SELECT oi.product.id, oi.product.name, SUM(oi.quantity) as totalSold FROM OrderItem oi WHERE oi.order.status = 'DELIVERED' GROUP BY oi.product.id, oi.product.name ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts(Pageable pageable);
}
