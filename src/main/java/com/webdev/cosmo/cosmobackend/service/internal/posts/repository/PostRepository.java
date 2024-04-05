package com.webdev.cosmo.cosmobackend.service.internal.posts.repository;

import com.webdev.cosmo.cosmobackend.service.api.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
}
