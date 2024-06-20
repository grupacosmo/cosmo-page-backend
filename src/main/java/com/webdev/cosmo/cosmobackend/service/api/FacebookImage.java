package com.webdev.cosmo.cosmobackend.service.api;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "facebook_images")
@Data
@Accessors(chain = true)
public class FacebookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer height;
    private Integer width;

    @Column(length = 250000)
    private String src;
}
