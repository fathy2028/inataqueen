package com.instaqueenback.instaqueen.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "offer_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Offer offer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;
}
