package com.webdev.cosmo.cosmobackend.service.api;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
@Entity
@Table(name = "users")
@Data
@Accessors(chain = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String surname;

    @Column(unique=true)
    private String email;

    private ROLE role;

    private LocalDateTime creation_date;
}
