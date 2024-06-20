package com.webdev.cosmo.cosmobackend.service.internal.posts.mapper;

import com.webdev.cosmo.cosmobackend.service.api.FacebookImage;
import com.webdev.cosmo.cosmobackend.service.api.Image;
import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.*;

import java.util.List;

@Mapper
public interface PostMapper {
    PostModel map(Post post);
    List<PostModel> map(List<Post> posts);
    Post map(PostModel postModel);
    UpdatePostRequest mapPostUpdate(Post post);

    default Post mapPostFromFacebookData(FacebookResponse facebookResponse) {
        return mapPostFromFacebookData(facebookResponse.getData().get(0));
    }

    @Mapping(source = "facebookDataItem.media.image", target = "facebookImages")
    @Mapping(source = "facebookDataItem.description", target = "description")
    Post mapPostFromFacebookData(FacebookDataItem facebookDataItem);

    default List<FacebookImage> map(FacebookPostImage value) {
        if(value == null)
            return List.of();

        return List.of(new FacebookImage()
                .setSrc(value.getSrc())
                .setWidth(value.getWidth())
                .setHeight(value.getHeight()));
    }
}
