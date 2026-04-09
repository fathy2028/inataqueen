package com.instaqueenback.instaqueen.service;

import com.instaqueenback.instaqueen.dto.request.OfferRequest;
import com.instaqueenback.instaqueen.dto.response.OfferResponse;
import com.instaqueenback.instaqueen.entity.Offer;
import com.instaqueenback.instaqueen.entity.OfferProduct;
import com.instaqueenback.instaqueen.entity.Product;
import com.instaqueenback.instaqueen.exception.ResourceNotFoundException;
import com.instaqueenback.instaqueen.repository.OfferRepository;
import com.instaqueenback.instaqueen.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    public List<OfferResponse> getAllOffers() {
        return offerRepository.findAll().stream().map(OfferResponse::from).toList();
    }

    public List<OfferResponse> getActiveOffers() {
        LocalDateTime now = LocalDateTime.now();
        return offerRepository.findByIsActiveTrueAndStartDateBeforeAndEndDateAfter(now, now)
                .stream().map(OfferResponse::from).toList();
    }

    public OfferResponse getOffer(UUID id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
        return OfferResponse.from(offer);
    }

    @Transactional
    public OfferResponse createOffer(OfferRequest request) {
        Offer offer = Offer.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .bannerImageUrl(request.getBannerImageUrl())
                .discountPercentage(request.getDiscountPercentage())
                .originalTotal(request.getOriginalTotal())
                .discountedTotal(request.getDiscountedTotal())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        offer = offerRepository.save(offer);

        if (request.getProducts() != null) {
            for (var p : request.getProducts()) {
                Product product = productRepository.findById(p.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + p.getProductId()));
                OfferProduct op = OfferProduct.builder()
                        .offer(offer).product(product).quantity(p.getQuantity()).build();
                offer.getOfferProducts().add(op);
            }
            offer = offerRepository.save(offer);
        }

        notificationService.sendOfferNotification(offer);
        return OfferResponse.from(offer);
    }

    @Transactional
    public OfferResponse updateOffer(UUID id, OfferRequest request) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
        offer.setTitle(request.getTitle());
        offer.setDescription(request.getDescription());
        offer.setBannerImageUrl(request.getBannerImageUrl());
        offer.setDiscountPercentage(request.getDiscountPercentage());
        offer.setOriginalTotal(request.getOriginalTotal());
        offer.setDiscountedTotal(request.getDiscountedTotal());
        offer.setStartDate(request.getStartDate());
        offer.setEndDate(request.getEndDate());

        offer.getOfferProducts().clear();
        if (request.getProducts() != null) {
            for (var p : request.getProducts()) {
                Product product = productRepository.findById(p.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
                offer.getOfferProducts().add(OfferProduct.builder()
                        .offer(offer).product(product).quantity(p.getQuantity()).build());
            }
        }
        return OfferResponse.from(offerRepository.save(offer));
    }

    public void deleteOffer(UUID id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
        offer.setActive(false);
        offerRepository.save(offer);
    }
}
