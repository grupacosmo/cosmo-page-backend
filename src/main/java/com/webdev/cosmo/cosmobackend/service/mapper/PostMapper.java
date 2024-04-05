package com.webdev.cosmo.cosmobackend.service.mapper;

import com.webdev.cosmo.cosmobackend.service.api.Post;
import org.mapstruct.Mapper;
import org.openapitools.model.PostModel;

import java.util.List;

@Mapper
public interface PostMapper {
    PostModel map(Post post);
    List<PostModel> map(List<Post> posts);
    Post map(PostModel postModel);
}
