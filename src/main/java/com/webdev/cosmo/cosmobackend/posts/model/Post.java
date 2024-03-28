package com.webdev.cosmo.cosmobackend.posts.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.List;

@Accessors(chain = true)
@Data
@NoArgsConstructor
public class Post {

    @Id
    private String id;
    private String title;
    private String description;
    private List<String> imageIds;
}
