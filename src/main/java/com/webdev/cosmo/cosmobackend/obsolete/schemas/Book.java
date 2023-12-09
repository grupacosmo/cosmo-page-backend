package com.webdev.cosmo.cosmobackend.obsolete.schemas;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class Book {

    @Id
    private Long id;
    private String title;
    private String author;
}
