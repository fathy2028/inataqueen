package com.instaqueenback.instaqueen.service;

import com.instaqueenback.instaqueen.dto.request.CategoryRequest;
import com.instaqueenback.instaqueen.dto.response.CategoryResponse;
import com.instaqueenback.instaqueen.entity.Category;
import com.instaqueenback.instaqueen.exception.ResourceNotFoundException;
import com.instaqueenback.instaqueen.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findByIsActiveTrue().stream().map(CategoryResponse::from).toList();
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();
        return CategoryResponse.from(categoryRepository.save(category));
    }

    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }
}
