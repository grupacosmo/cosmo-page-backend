package com.webdev.cosmo.cosmobackend.posts.repository;

import com.webdev.cosmo.cosmobackend.posts.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
