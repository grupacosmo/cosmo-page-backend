package com.webdev.cosmo.cosmobackend.service.internal.posts.mapper;

import com.webdev.cosmo.cosmobackend.service.api.FacebookImage;
import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import org.apache.commons.lang3.tuple.Pair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.*;

import java.util.Collection;
import java.util.List;

@Mapper
public abstract class PostMapper {
    public abstract PostModel map(Post post);
    public abstract List<PostModel> map(List<Post> posts);
    public abstract Post map(PostModel postModel);

    @Mapping(source = "facebookDataItem.media.image", target = "facebookImages")
    @Mapping(source = "facebookDataItem.message", target = "description")
    @Mapping(source = "facebookDataItem.id", target = "providerId")
    public abstract Post mapPostFromFacebookData(FacebookDataItem facebookDataItem);

    public List<FacebookImage> map(FacebookPostImage value) {
        if(value == null)
            return List.of();

        return List.of(new FacebookImage()
                .setSrc(value.getSrc())
                .setWidth(value.getWidth())
                .setHeight(value.getHeight()));
    }

    public Post mapPostFromFacebookData(Pair<FacebookDataItem, FacebookResponse> facebookResponseFacebookResponsePair) {
        var facebookPostData = facebookResponseFacebookResponsePair.getLeft();
        var facebookAttachments = facebookResponseFacebookResponsePair.getRight();

        Post post = this.mapPostFromFacebookData(facebookPostData);

        if(facebookAttachments.getData().size() < 1) {
            return post;
        }

        if( facebookAttachments.getData().get(0).getSubattachments() == null) {

            if(facebookAttachments.getData().get(0).getMedia() != null) {
                return post.setFacebookImages(map(facebookAttachments.getData().get(0).getMedia().getImage()));
            }

            return post;
        }

        var images = facebookAttachments.getData().get(0)
                .getSubattachments()
                .getData().stream()
                .map(FacebookDataItem::getMedia)
                .map(FacebookPostMedia::getImage)
                .map(this::map)
                .flatMap(Collection::stream)
                .toList();

        return  post.setFacebookImages(images);
    }
}
