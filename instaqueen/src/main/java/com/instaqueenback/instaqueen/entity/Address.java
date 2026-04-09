package com.instaqueenback.instaqueen.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String label;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    private String state;
    private String zipCode;
    private Double latitude;
    private Double longitude;

    @Builder.Default
    private boolean isDefault = false;
}
