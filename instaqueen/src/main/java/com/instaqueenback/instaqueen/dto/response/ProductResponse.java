package com.instaqueenback.instaqueen.dto.response;

import com.instaqueenback.instaqueen.entity.Product;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private CategoryResponse category;
    private int stock;
    private boolean isActive;
    private LocalDateTime createdAt;

    public static ProductResponse from(Product p) {
        return ProductResponse.builder()
                .id(p.getId()).name(p.getName()).description(p.getDescription())
                .price(p.getPrice()).imageUrl(p.getImageUrl())
                .category(p.getCategory() != null ? CategoryResponse.from(p.getCategory()) : null)
                .stock(p.getStock()).isActive(p.isActive()).createdAt(p.getCreatedAt())
                .build();
    }
}
