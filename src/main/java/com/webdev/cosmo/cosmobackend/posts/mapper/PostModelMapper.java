package com.webdev.cosmo.cosmobackend.posts.mapper;

import com.webdev.cosmo.cosmobackend.posts.dto.PostModel;
import com.webdev.cosmo.cosmobackend.posts.model.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PostModelMapper {
    PostModel simpleMap(Post post);
    Post modelToPost(PostModel postModel);
    List<PostModel> map(List<Post> posts);
}
