package com.webdev.cosmo.cosmobackend.service.internal.posts;

import com.webdev.cosmo.cosmobackend.service.internal.posts.service.PageablePostsService;
import com.webdev.cosmo.cosmobackend.service.internal.posts.service.PostDetailsQueryService;
import com.webdev.cosmo.cosmobackend.service.internal.posts.service.PostService;
import com.webdev.cosmo.cosmobackend.service.internal.posts.service.PostsSyncExecutor;
import com.webdev.cosmo.cosmobackend.service.internal.posts.service.UpdatePostService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PostListQueryItem;
import org.openapitools.model.PostListQueryItemDetails;
import org.openapitools.model.PostModel;
import org.openapitools.model.PostRequest;
import org.openapitools.model.UpdatePostRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService service;
    private final UpdatePostService updatePostService;
    private final PostsSyncExecutor postsSyncExecutor;
    private final PageablePostsService pageablePostsService;
    private final PostDetailsQueryService postDetailsQueryService;


    @PutMapping("/sync")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void syncPostsWithFacebook() {
        postsSyncExecutor.execute();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostModel createPost(@RequestBody PostRequest postRequest) {
        return service.createPost(postRequest);
    }

    @GetMapping("/{postId}")
    @Transactional(readOnly = true)
    public PostListQueryItemDetails getPostById(@PathVariable String postId) {
        return postDetailsQueryService.findBy(postId);
    }

    @GetMapping
    public Page<PostListQueryItem> getAllPosts(@RequestParam int page, @RequestParam int size) {
        return pageablePostsService.findAll(page, size);
    }

    @PutMapping("/{postId}")
    public PostModel updatePost(@PathVariable String postId,
                                @RequestBody UpdatePostRequest updatePostRequest) {
        return updatePostService.update(updatePostRequest, postId);
    }

    @DeleteMapping("/{postId}")
    public Map<String, String> deletePost(@PathVariable String postId) {
        service.deletePost(postId);
        return Map.of("id", postId);
    }
}
