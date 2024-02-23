package com.webdev.cosmo.cosmobackend.posts.service;

import com.webdev.cosmo.cosmobackend.posts.dto.PostModel;
import com.webdev.cosmo.cosmobackend.posts.exception.NotFoundException;
import com.webdev.cosmo.cosmobackend.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.posts.model.Post;
import com.webdev.cosmo.cosmobackend.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final PostMapper mapper;
    @Override
    public PostModel createPost(PostModel postModel) {

        Post savedPost = repository.save(mapper.map(postModel));

        return mapper.map(savedPost);
    }

    @Override
    public PostModel getPostById(String id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        return mapper.map(post);
    }

    @Override
    public List<PostModel> getAllPosts() {
        List<Post> posts = repository.findAll();

        return mapper.map(posts);
    }

    @Override
    public PostModel updatePost(String id, Post post) {
        Post existingPost = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        existingPost.setTitle(post.getTitle())
                    .setDescription(post.getDescription())
                    .setImageIds(post.getImageIds());

        Post updatedPost = repository.save(existingPost);

        return mapper.map(updatedPost);
    }

    @Override
    public void deletePost(String id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        repository.delete(post);
    }
}
