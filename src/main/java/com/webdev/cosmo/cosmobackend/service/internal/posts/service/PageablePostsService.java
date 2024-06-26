package com.webdev.cosmo.cosmobackend.service.internal.posts.service;

import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import com.webdev.cosmo.cosmobackend.util.interfaces.PageableQueryService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PostListQueryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageablePostsService implements PageableQueryService<PostListQueryItem> {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public Page<PostListQueryItem> findAll(int page, int size) {
        return postRepository.findAll(PageRequest.of(page, size))
                .map(postMapper::mapPostListQueryItem);
    }
}
