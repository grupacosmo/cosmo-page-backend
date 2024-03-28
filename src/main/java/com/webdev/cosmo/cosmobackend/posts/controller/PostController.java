package com.webdev.cosmo.cosmobackend.posts.controller;

import com.webdev.cosmo.cosmobackend.posts.model.Post;
import com.webdev.cosmo.cosmobackend.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PostModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PostModel> createPost(@RequestBody Post post) {
        return service.createPost(post);
    }

    @GetMapping("/{postId}")
    public Mono<PostModel> getPostById(@PathVariable String postId) {
        return service.getPostById(postId);
    }

    @GetMapping
    public Flux<PostModel> getAllPosts() {
        return service.getAllPosts();
    }

    @PutMapping("/{postId}")
    public Mono<PostModel> updatePost(@PathVariable String postId,
                                @RequestBody Post post) {
        return service.updatePost(postId, post);
    }

    @DeleteMapping("/{postId}")
    public Map<String, String> deletePost(@PathVariable String postId) {
        service.deletePost(postId);
        return new HashMap<>() {{
            put("id", postId);
        }};
    }
}
