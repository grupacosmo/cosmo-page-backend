package com.webdev.cosmo.cosmobackend.posts.service;

import com.webdev.cosmo.cosmobackend.posts.dto.PostModel;
import com.webdev.cosmo.cosmobackend.posts.exception.NotFoundException;
import com.webdev.cosmo.cosmobackend.posts.mapper.PostModelMapper;
import com.webdev.cosmo.cosmobackend.posts.model.Post;
import com.webdev.cosmo.cosmobackend.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final PostModelMapper mapper;
    @Override
    public PostModel createPost(PostModel postModel) {

        Post savedPost = repository.save(mapper.modelToPost(postModel));
        PostModel savedPostModel = mapper.simpleMap(savedPost);

        return savedPostModel;
    }

    @Override
    public PostModel getPostById(Long id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        return mapper.simpleMap(post);
    }

    @Override
    public List<PostModel> getAllPosts() {
        List<Post> posts = repository.findAll();

        return mapper.map(posts);
    }

    @Override
    public PostModel updatePost(Long id, PostModel postModel) {
        Post existingPost = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        existingPost.setTitle(postModel.getTitle());
        existingPost.setDescription(postModel.getDescription());
        existingPost.setImageId(postModel.getImageId());
        Post updatedPost = repository.save(existingPost);

        return mapper.simpleMap(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        repository.delete(post);
    }
}
