package com.webdev.cosmo.cosmobackend.service.internal.posts.config;

import com.webdev.cosmo.cosmobackend.service.internal.image.mapper.ImageMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import com.webdev.cosmo.cosmobackend.service.internal.posts.service.PostService;
import com.webdev.cosmo.cosmobackend.service.internal.posts.service.PostServiceImpl;
import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.service.UpdatePostService;
import com.webdev.cosmo.cosmobackend.util.interfaces.UpdateService;
import org.openapitools.model.PostModel;
import org.openapitools.model.UpdatePostRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostConfig {

    @Bean
    public PostService postService(PostRepository repository, PostMapper mapper) {
        return new PostServiceImpl(repository, mapper);
    }

    @Bean
    public UpdateService<UpdatePostRequest, PostModel, String> updatePostService(
            final PostMapper postMapper,
            final ImageMapper imageMapper,
            final PostRepository postRepository
            ) {
        return new UpdatePostService(postMapper, imageMapper, postRepository);
    }
}
