package com.webdev.cosmo.cosmobackend.posts.service;

import com.webdev.cosmo.cosmobackend.posts.model.Post;

import java.util.function.Function;

public class PostUpdater implements Function<Post, Post> {
    @Override
    public Post apply(Post post) {
        return post.setTitle(post.getTitle())
                .setDescription(post.getDescription())
                .setImageIds(post.getImageIds());
    }
}
