package com.webdev.cosmo.cosmobackend.service.internal.posts.service;

import com.webdev.cosmo.cosmobackend.service.internal.image.mapper.ImageMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PostModel;
import org.openapitools.model.UpdatePostRequest;

import java.util.Optional;

import static com.webdev.cosmo.cosmobackend.error.Error.POST_NOT_FOUND;

@RequiredArgsConstructor
public class UpdatePostService {

    private final PostMapper postMapper;
    private final ImageMapper imageMapper;
    private final PostRepository postRepository;

    public PostModel update(UpdatePostRequest updatePostRequest, String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(POST_NOT_FOUND::getError);

        updatePost(updatePostRequest, post);

        return postMapper.map(postRepository.save(post));
    }

    private void updatePost(UpdatePostRequest updatePostRequest, Post post) {
        Optional.ofNullable(updatePostRequest.getDescription())
                .ifPresent(post::setDescription);

        Optional.ofNullable(updatePostRequest.getTitle())
                .ifPresent(post::setTitle);

        Optional.ofNullable(updatePostRequest.getImages())
                .map(imageMapper::mapToEntity)
                .ifPresent(post::setImages);
    }
}