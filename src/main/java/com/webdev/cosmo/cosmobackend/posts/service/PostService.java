package com.webdev.cosmo.cosmobackend.posts.service;

import com.webdev.cosmo.cosmobackend.posts.dto.PostModel;

import java.util.List;

public interface PostService {
    PostModel createPost(PostModel postModel);
    PostModel getPostById(Long id);
    List<PostModel> getAllPosts();
    PostModel updatePost(Long id, PostModel postModel);
    void deletePost(Long id);
}
