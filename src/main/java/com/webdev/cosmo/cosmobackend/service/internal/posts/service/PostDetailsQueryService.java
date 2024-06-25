package com.webdev.cosmo.cosmobackend.service.internal.posts.service;

import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import com.webdev.cosmo.cosmobackend.util.interfaces.SimpleQueryService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PostListQueryItemDetails;
import org.springframework.stereotype.Service;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_POST_DATA;

@Service
@RequiredArgsConstructor
public class PostDetailsQueryService implements SimpleQueryService<String, PostListQueryItemDetails> {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public PostListQueryItemDetails findBy(String postId) {
        return postRepository.findById(postId)
                .map(postMapper::mapPostListQueryItemDetails)
                .orElseThrow(INVALID_POST_DATA::getError);
    }
}
