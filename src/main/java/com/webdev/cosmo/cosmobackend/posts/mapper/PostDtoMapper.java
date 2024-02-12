package com.webdev.cosmo.cosmobackend.posts.mapper;

import com.webdev.cosmo.cosmobackend.posts.dto.PostDTO;
import com.webdev.cosmo.cosmobackend.posts.model.Post;
import org.mapstruct.Mapper;

@Mapper
public interface PostDtoMapper {
    PostDTO postToDto(Post post);
    Post dtoToPost(PostDTO postDTO);
}
