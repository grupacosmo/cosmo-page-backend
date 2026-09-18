package com.webdev.cosmo.cosmobackend.service.internal.posts.service;

import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import org.openapitools.model.PostModel;
import org.openapitools.model.PostRequest;

import java.util.List;

public interface PostService {
    PostModel createPost(PostRequest postRequest);
    PostModel getPostById(String id);
    List<PostModel> getAllPosts();
    PostModel updatePost(String id, Post post);
    void deletePost(String id);
}
