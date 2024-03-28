package com.webdev.cosmo.cosmobackend.posts.service;

import com.webdev.cosmo.cosmobackend.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.posts.model.Post;
import com.webdev.cosmo.cosmobackend.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PostModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_REQUEST;

@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final PostMapper mapper;
    private final Function<Post, Post> postUpdater;

    @Override
    public Mono<PostModel> createPost(Post post) {
        return repository.save(post)
                .map(mapper::map);
    }

    @Override
    public Mono<PostModel> getPostById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(INVALID_REQUEST.getError()))
                .map(mapper::map);
    }

    @Override
    public Flux<PostModel> getAllPosts() {
        return repository.findAll()
                .map(mapper::map);
    }

    @Override
    public Mono<PostModel> updatePost(String id, Post post) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(INVALID_REQUEST.getError()))
                .map(postUpdater::apply)
                .flatMap(repository::save)
                .map(mapper::map);
    }

    @Override
    public void deletePost(String id) {
        repository.deleteById(id);
    }
}
