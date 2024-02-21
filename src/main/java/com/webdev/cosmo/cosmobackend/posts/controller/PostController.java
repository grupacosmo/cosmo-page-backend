package com.webdev.cosmo.cosmobackend.posts.controller;

import com.webdev.cosmo.cosmobackend.posts.dto.PostModel;
import com.webdev.cosmo.cosmobackend.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostModel createPost(@RequestBody PostModel post) {
        return service.createPost(post);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostModel getPostById(@PathVariable Long postId) {
        return service.getPostById(postId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostModel> getAllPosts() {
        return service.getAllPosts();
    }

    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostModel updatePost(@PathVariable Long postId,
                                @RequestBody PostModel postModel) {
        return service.updatePost(postId, postModel);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePost(@PathVariable Long postId) {
        service.deletePost(postId);
        return "id: " + postId;
    }
}
