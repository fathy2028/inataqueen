package com.instaqueenback.instaqueen.controller;

import com.instaqueenback.instaqueen.dto.request.OfferRequest;
import com.instaqueenback.instaqueen.dto.response.*;
import com.instaqueenback.instaqueen.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OfferResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(offerService.getAllOffers()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<OfferResponse>>> getActive() {
        return ResponseEntity.ok(ApiResponse.ok(offerService.getActiveOffers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OfferResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(offerService.getOffer(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OfferResponse>> create(@Valid @RequestBody OfferRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Offer created", offerService.createOffer(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OfferResponse>> update(@PathVariable UUID id, @Valid @RequestBody OfferRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Offer updated", offerService.updateOffer(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        offerService.deleteOffer(id);
        return ResponseEntity.ok(ApiResponse.ok("Offer deleted", null));
    }
}
