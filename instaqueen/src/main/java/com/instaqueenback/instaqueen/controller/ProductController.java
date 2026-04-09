package com.instaqueenback.instaqueen.controller;

import com.instaqueenback.instaqueen.dto.request.ProductRequest;
import com.instaqueenback.instaqueen.dto.response.*;
import com.instaqueenback.instaqueen.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAll(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) UUID categoryId) {
        var page = productService.getAllProducts(pageable, categoryId);
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.from(page, p -> p)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> search(
            @RequestParam String q, @PageableDefault(size = 20) Pageable pageable) {
        var page = productService.searchProducts(q, pageable);
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.from(page, p -> p)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(productService.getProduct(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Product created", productService.createProduct(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(@PathVariable UUID id, @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Product updated", productService.updateProduct(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.ok("Product deleted", null));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> lowStock() {
        return ResponseEntity.ok(ApiResponse.ok(productService.getLowStockProducts()));
    }
}
