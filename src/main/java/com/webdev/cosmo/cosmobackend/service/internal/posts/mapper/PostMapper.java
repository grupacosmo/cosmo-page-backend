package com.webdev.cosmo.cosmobackend.service.internal.posts.mapper;

import com.webdev.cosmo.cosmobackend.service.api.FacebookImage;
import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import org.apache.commons.lang3.tuple.Pair;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mapper(uses = {FacebookImageMapper.class})
public abstract class PostMapper {

    @Autowired
    protected FacebookImageMapper facebookImageMapper;

    public abstract PostModel map(Post post);
    public abstract List<PostModel> map(List<Post> posts);
    public abstract Post map(PostModel postModel);

    public abstract PostListQueryItem mapPostListQueryItem(Post post);

    public abstract PostListQueryItemDetails mapPostListQueryItemDetails(Post post);

    @AfterMapping
    protected void afterMapPostListQueryItem(@MappingTarget PostListQueryItem postListQueryItem, Post post) {
        Optional.of(post)
                .map(Post::getFacebookImages)
                .orElse(Collections.emptyList())
                .stream().findFirst()
                .map(facebookImageMapper::map)
                .ifPresent(postListQueryItem::setBackgroundPhoto);
    }

    @AfterMapping
    protected void afterMapPostListQueryItem(@MappingTarget PostListQueryItemDetails postListQueryItemDetails, Post post) {
        var postsImages = Optional.of(post)
                .map(Post::getFacebookImages)
                .orElse(Collections.emptyList())
                .stream()
                .map(facebookImageMapper::map)
                .toList();

        postListQueryItemDetails.setImages(postsImages);
    }

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
