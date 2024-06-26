package com.webdev.cosmo.cosmobackend.service.internal.posts;

import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import com.webdev.cosmo.cosmobackend.service.internal.posts.service.PostService;
import com.webdev.cosmo.cosmobackend.util.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PostListQueryItem;
import org.openapitools.model.PostListQueryItemDetails;
import org.openapitools.model.PostModel;
import org.openapitools.model.UpdatePostRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService service;
    private final UpdateService<UpdatePostRequest, PostModel, String> updatePostService;
    private final Executor postsSyncExecutor;
    private final PageableQueryService<PostListQueryItem> pageablePostsService;
    private final SimpleQueryService<String, PostListQueryItemDetails> postDetailsQueryService;


    @PostMapping("/sync")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void syncPostsWithFacebook() {
        postsSyncExecutor.execute();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostModel createPost(@RequestBody Post post) {
        return service.createPost(post);
    }

    @GetMapping("/{postId}")
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
        return new HashMap<>() {{
            put("id", postId);
        }};
    }
}
