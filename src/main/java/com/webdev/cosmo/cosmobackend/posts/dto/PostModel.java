package com.webdev.cosmo.cosmobackend.posts.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PostModel {
    private String title;
    private String description;
    private Long imageId;
}
