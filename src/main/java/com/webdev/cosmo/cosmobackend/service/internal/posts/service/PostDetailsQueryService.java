package com.webdev.cosmo.cosmobackend.service.internal.posts.service;

import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PostListQueryItemDetails;
import org.springframework.stereotype.Service;

import static com.webdev.cosmo.cosmobackend.error.Error.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostDetailsQueryService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostListQueryItemDetails findBy(String postId) {
        return postRepository.findById(postId)
                .map(postMapper::mapPostListQueryItemDetails)
                .orElseThrow(POST_NOT_FOUND::getError);
    }
}
