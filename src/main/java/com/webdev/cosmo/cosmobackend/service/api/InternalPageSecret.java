package com.webdev.cosmo.cosmobackend.service.api;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Entity
@Table(name = "facebook_internal_profile")
@Data
@Accessors(chain = true)
public class InternalPageSecret {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String secret;

    private OffsetDateTime validityPeriod;
}
