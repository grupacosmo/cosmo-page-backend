package com.webdev.cosmo.cosmobackend.service.internal.posts.model;

import com.webdev.cosmo.cosmobackend.service.api.FacebookImage;
import com.webdev.cosmo.cosmobackend.service.api.Image;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
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

    private String providerId;

    @Column
    private String title;
    @Column(length = 250000)
    private String description;

    @OneToMany
    @Column
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "posts_facebook_images",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "facebook_images_id")
    )
    private List<FacebookImage> facebookImages = new ArrayList<>();

    public List<FacebookImage> getFacebookImages() {
        if (facebookImages == null) {
            facebookImages = new ArrayList<>();
        }
        return facebookImages;
    }

}
