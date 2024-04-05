package com.webdev.cosmo.cosmobackend.service.internal.posts.config;

import com.webdev.cosmo.cosmobackend.service.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import com.webdev.cosmo.cosmobackend.service.internal.posts.service.PostService;
import com.webdev.cosmo.cosmobackend.service.internal.posts.service.PostServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostConfig {

    @Bean
    public PostService postService(PostRepository repository, PostMapper mapper) {
        return new PostServiceImpl(repository, mapper);
    }
}
