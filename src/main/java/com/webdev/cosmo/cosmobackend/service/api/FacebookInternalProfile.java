package com.webdev.cosmo.cosmobackend.service.api;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "facebook_internal_profile")
@Data
@Accessors(chain = true)
public class FacebookInternalProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private Profile name;

    @Column(unique = true)
    private String clientId;

    private String secret;

    @Column(unique = true)
    private String pageId;
    private LocalDateTime validityPeriod;
}
