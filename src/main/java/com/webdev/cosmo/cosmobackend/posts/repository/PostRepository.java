package com.webdev.cosmo.cosmobackend.posts.repository;

import com.webdev.cosmo.cosmobackend.posts.model.Post;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends R2dbcRepository<Post, String> {
}
