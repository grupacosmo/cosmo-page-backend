package com.webdev.cosmo.cosmobackend.posts.service;

import com.webdev.cosmo.cosmobackend.posts.dto.PostDTO;
import com.webdev.cosmo.cosmobackend.posts.exception.NotFoundException;
import com.webdev.cosmo.cosmobackend.posts.mapper.PostDtoMapper;
import com.webdev.cosmo.cosmobackend.posts.model.Post;
import com.webdev.cosmo.cosmobackend.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository repository;
    private final PostDtoMapper mapper;
    @Override
    public PostDTO createPost(PostDTO postDTO) {

        Post post = mapper.dtoToPost(postDTO);

        Post savedPost = repository.save(post);
        PostDTO savedPostDTO = mapper.postToDto(savedPost);

        return savedPostDTO;
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        return mapper.postToDto(post);
    }

    @Override
    public List<PostDTO> getAllPosts() {
        List<Post> posts = repository.findAll();

        return posts.stream()
                .map(post -> mapper.postToDto(post))
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO updatePost(Long id, PostDTO postDTO) {
        Post existingPost = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        existingPost.setTitle(postDTO.getTitle());
        existingPost.setDescription(postDTO.getDescription());
        existingPost.setImageId(postDTO.getImageId());
        Post updatedPost = repository.save(existingPost);

        return mapper.postToDto(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        repository.delete(post);
    }
}
