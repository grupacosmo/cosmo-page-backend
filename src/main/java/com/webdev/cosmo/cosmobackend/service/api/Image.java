package com.webdev.cosmo.cosmobackend.service.api;


import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "images")
@Data
@Accessors(chain = true)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String type;

    @Lob
    private byte[] data;
}
