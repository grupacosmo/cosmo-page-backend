package com.webdev.cosmo.cosmobackend.service.internal.posts.service;

import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PostModel;

import java.util.List;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_REQUEST;

@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final PostMapper mapper;

    @Override
    public PostModel createPost(Post post) {
        return mapper.map(repository.save(post));
    }

    @Override
    public PostModel getPostById(String id) {
        Post post = repository.findById(id)
                .orElseThrow(INVALID_REQUEST::getError);

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
                .orElseThrow(INVALID_REQUEST::getError);

        existingPost.setTitle(post.getTitle())
                    .setDescription(post.getDescription());
//                    .setImageIds(post.getImageIds());

        Post updatedPost = repository.save(existingPost);

        return mapper.map(updatedPost);
    }

    @Override
    public void deletePost(String id) {
        Post post = repository.findById(id)
                .orElseThrow(INVALID_REQUEST::getError);

        repository.delete(post);
    }
}
