package com.instaqueenback.instaqueen.repository;

import com.instaqueenback.instaqueen.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {
    List<Offer> findByIsActiveTrue();
    List<Offer> findByIsActiveTrueAndStartDateBeforeAndEndDateAfter(LocalDateTime now1, LocalDateTime now2);
    long countByIsActiveTrue();
}
