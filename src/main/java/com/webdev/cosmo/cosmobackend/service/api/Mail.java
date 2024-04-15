package com.webdev.cosmo.cosmobackend.service.api;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "mail_history")
@Data
@Accessors(chain = true)
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String recipientEmail;
    private String action;
    private OffsetDateTime timestamp;
}
