package com.webdev.cosmo.cosmobackend.posts.mapper;

import com.webdev.cosmo.cosmobackend.posts.model.Post;
import org.mapstruct.Mapper;
import org.openapitools.model.PostModel;

import java.util.List;

@Mapper
public interface PostMapper {
    PostModel map(Post post);
    List<PostModel> map(List<Post> posts);
    Post map(PostModel postModel);
}
