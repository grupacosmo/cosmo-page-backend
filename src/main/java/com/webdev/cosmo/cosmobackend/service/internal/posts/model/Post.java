package com.webdev.cosmo.cosmobackend.service.internal.posts.model;

import com.webdev.cosmo.cosmobackend.service.api.FacebookImage;
import com.webdev.cosmo.cosmobackend.service.api.Image;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.openapitools.model.FacebookPostImage;

import java.util.List;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String title;
    @Column(length = 250000)
    private String description;

    @OneToMany
    @Column
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL)
    @Column
    private List<FacebookImage> facebookImages;
}
