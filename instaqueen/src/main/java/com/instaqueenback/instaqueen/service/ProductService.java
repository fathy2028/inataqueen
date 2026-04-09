package com.instaqueenback.instaqueen.service;

import com.instaqueenback.instaqueen.dto.request.ProductRequest;
import com.instaqueenback.instaqueen.dto.response.ProductResponse;
import com.instaqueenback.instaqueen.entity.Category;
import com.instaqueenback.instaqueen.entity.Product;
import com.instaqueenback.instaqueen.exception.ResourceNotFoundException;
import com.instaqueenback.instaqueen.repository.CategoryRepository;
import com.instaqueenback.instaqueen.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<ProductResponse> getAllProducts(Pageable pageable, UUID categoryId) {
        Page<Product> page;
        if (categoryId != null) {
            page = productRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable);
        } else {
            page = productRepository.findByIsActiveTrue(pageable);
        }
        return page.map(ProductResponse::from);
    }

    public Page<ProductResponse> searchProducts(String query, Pageable pageable) {
        return productRepository.searchByNameOrDescription(query, pageable).map(ProductResponse::from);
    }

    public ProductResponse getProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return ProductResponse.from(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .stock(request.getStock())
                .build();
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }
        return ProductResponse.from(productRepository.save(product));
    }

    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setStock(request.getStock());
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }
        return ProductResponse.from(productRepository.save(product));
    }

    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }

    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findByStockLessThanEqualAndIsActiveTrue(2)
                .stream().map(ProductResponse::from).toList();
    }
}
