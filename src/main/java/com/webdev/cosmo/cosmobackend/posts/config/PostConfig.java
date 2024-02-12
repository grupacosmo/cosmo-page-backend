package com.webdev.cosmo.cosmobackend.posts.config;

import com.webdev.cosmo.cosmobackend.posts.mapper.PostDtoMapper;
import com.webdev.cosmo.cosmobackend.posts.repository.PostRepository;
import com.webdev.cosmo.cosmobackend.posts.service.PostServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostConfig {
    @Bean
    public PostServiceImpl postService(PostRepository repository, PostDtoMapper mapper) {
        return new PostServiceImpl(repository, mapper);
    }
}
