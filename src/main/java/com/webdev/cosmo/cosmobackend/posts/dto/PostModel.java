package com.webdev.cosmo.cosmobackend.posts.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class PostModel {
    private String id;
    private String title;
    private String description;
    private List<String> imageIds;
}
