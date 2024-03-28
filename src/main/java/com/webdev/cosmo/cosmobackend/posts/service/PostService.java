package com.webdev.cosmo.cosmobackend.posts.service;

import com.webdev.cosmo.cosmobackend.posts.model.Post;
import org.openapitools.model.PostModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PostService {
    Mono<PostModel> createPost(Post post);
    Mono<PostModel> getPostById(String id);
    Flux<PostModel> getAllPosts();
    Mono<PostModel> updatePost(String id, Post post);
    void deletePost(String id);
}
