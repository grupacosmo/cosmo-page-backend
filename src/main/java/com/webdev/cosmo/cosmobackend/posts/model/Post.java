package com.webdev.cosmo.cosmobackend.posts.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @UuidGenerator
    private String id;

    @Column
    private String title;
    @Column
    private String description;
    @Column
    private List<String> imageIds;
}
