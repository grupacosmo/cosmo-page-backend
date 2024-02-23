package com.webdev.cosmo.cosmobackend.posts.service;

import com.webdev.cosmo.cosmobackend.posts.dto.PostModel;
import com.webdev.cosmo.cosmobackend.posts.model.Post;

import java.util.List;

public interface PostService {
    PostModel createPost(PostModel postModel);
    PostModel getPostById(String id);
    List<PostModel> getAllPosts();
    PostModel updatePost(String id, Post post);
    void deletePost(String id);
}
