package com.webdev.cosmo.cosmobackend.service.internal.posts.service;

import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import com.webdev.cosmo.cosmobackend.util.interfaces.ListQueryService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PostListQueryItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostListQueryService implements ListQueryService<PostListQueryItem> {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public List<PostListQueryItem> findAll() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapPostListQueryItem)
                .toList();
    }
}
