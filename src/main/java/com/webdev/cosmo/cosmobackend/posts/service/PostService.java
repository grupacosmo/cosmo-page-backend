package com.webdev.cosmo.cosmobackend.posts.service;

import com.webdev.cosmo.cosmobackend.posts.model.Post;
import org.openapitools.model.PostModel;

import java.util.List;

public interface PostService {
    PostModel createPost(Post post);
    PostModel getPostById(String id);
    List<PostModel> getAllPosts();
    PostModel updatePost(String id, Post post);
    void deletePost(String id);
}
