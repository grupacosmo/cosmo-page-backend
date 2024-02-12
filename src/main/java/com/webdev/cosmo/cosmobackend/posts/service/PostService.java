package com.webdev.cosmo.cosmobackend.posts.service;

import com.webdev.cosmo.cosmobackend.posts.dto.PostDTO;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    PostDTO getPostById(Long id);
    List<PostDTO> getAllPosts();
    PostDTO updatePost(Long id, PostDTO postDTO);
    void deletePost(Long id);
}
