package com.instaqueenback.instaqueen.dto.response;

import com.instaqueenback.instaqueen.entity.Category;
import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private UUID id;
    private String name;
    private String description;
    private String imageUrl;

    public static CategoryResponse from(Category c) {
        return CategoryResponse.builder()
                .id(c.getId()).name(c.getName())
                .description(c.getDescription()).imageUrl(c.getImageUrl())
                .build();
    }
}
