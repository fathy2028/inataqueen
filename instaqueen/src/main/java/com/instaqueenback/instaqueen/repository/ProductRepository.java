package com.instaqueenback.instaqueen.repository;

import com.instaqueenback.instaqueen.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByIsActiveTrue(Pageable pageable);
    Page<Product> findByCategoryIdAndIsActiveTrue(UUID categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Product> searchByNameOrDescription(@Param("q") String query, Pageable pageable);

    List<Product> findByStockLessThanEqualAndIsActiveTrue(int stock);
    List<Product> findByIdInAndIsActiveTrue(List<UUID> ids);
    long countByIsActiveTrue();
}
