package com.webdev.cosmo.cosmobackend.service.internal.posts.service;

import com.webdev.cosmo.cosmobackend.service.internal.image.mapper.ImageMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import com.webdev.cosmo.cosmobackend.util.BetterOptional;
import com.webdev.cosmo.cosmobackend.util.interfaces.UpdateService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PostModel;
import org.openapitools.model.UpdatePostRequest;

import java.util.Collections;
import java.util.Optional;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_POST_DATA;
import static com.webdev.cosmo.cosmobackend.error.Error.POST_NOT_FOUND;

@RequiredArgsConstructor
public class UpdatePostService implements UpdateService<UpdatePostRequest, PostModel, String> {

    private final PostMapper postMapper;
    private final ImageMapper imageMapper;
    private final PostRepository postRepository;

    @Override
    public PostModel update(UpdatePostRequest updatePostRequest, String id) {

        return BetterOptional.fromOptional(postRepository.findById(id), POST_NOT_FOUND.getError())
                .peek(post -> updatePost(updatePostRequest, post))
                .peek(postRepository::save)
                .optionalMap(postMapper::map)
                .orElseThrow(INVALID_POST_DATA::getError);
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
