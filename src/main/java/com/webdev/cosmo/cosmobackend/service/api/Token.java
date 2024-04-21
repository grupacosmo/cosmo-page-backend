package com.webdev.cosmo.cosmobackend.service.api;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "token")
@Data
@Accessors(chain = true)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String value;
    private String validityPeriod;

    @ManyToOne
    private User addedBy;
}
