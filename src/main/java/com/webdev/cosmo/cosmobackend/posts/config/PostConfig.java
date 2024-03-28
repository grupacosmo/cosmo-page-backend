package com.webdev.cosmo.cosmobackend.posts.config;

import com.webdev.cosmo.cosmobackend.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.posts.model.Post;
import com.webdev.cosmo.cosmobackend.posts.repository.PostRepository;
import com.webdev.cosmo.cosmobackend.posts.service.PostService;
import com.webdev.cosmo.cosmobackend.posts.service.PostServiceImpl;
import com.webdev.cosmo.cosmobackend.posts.service.PostUpdater;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class PostConfig {

    @Bean
    public PostService postService(
            final PostRepository repository,
            final PostMapper mapper,
            final Function<Post, Post> postUpdater
            ) {
        return new PostServiceImpl(repository, mapper, postUpdater);
    }

    @Bean
    public Function<Post, Post> postUpdater() {
        return new PostUpdater();
    }
}
