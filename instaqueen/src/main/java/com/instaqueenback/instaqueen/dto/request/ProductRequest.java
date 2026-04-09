package com.instaqueenback.instaqueen.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank
    private String name;
    private String description;
    @NotNull @DecimalMin("0.01")
    private BigDecimal price;
    private String imageUrl;
    private UUID categoryId;
    @Min(0)
    private int stock;
}
