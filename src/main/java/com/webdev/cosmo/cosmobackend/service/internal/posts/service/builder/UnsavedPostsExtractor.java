package com.webdev.cosmo.cosmobackend.service.internal.posts.service.builder;

import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.model.FacebookDataItem;
import org.openapitools.model.FacebookPostImage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Slf4j
@Component
public class UnsavedPostsExtractor implements BiFunction<List<FacebookDataItem>, List<Post>, List<FacebookDataItem>> {

    @Override
    public List<FacebookDataItem> apply(List<FacebookDataItem> facebookDataItems, List<Post> posts) {
        return facebookDataItems.stream().filter(item -> posts.stream().noneMatch(post -> {
            if (!StringUtils.equals(post.getProviderId(), item.getId())) {
                return false;
            }

            if (post.getDescription() != null && !post.getDescription().equals(item.getMessage())) {
                return false;
            }

            List<FacebookPostImage> facebookPostImages = new ArrayList<>();

            if (item.getMedia() != null && item.getMedia().getImage() != null) {
                facebookPostImages.add(item.getMedia().getImage());
            }

            if (item.getSubattachments() != null) {
                item.getSubattachments().getData().forEach(subItem -> {
                    if (subItem.getMedia() != null && subItem.getMedia().getImage() != null) {
                        facebookPostImages.add(subItem.getMedia().getImage());
                    }
                });
            }

            return facebookPostImages.size() == post.getFacebookImages().size() && post.getFacebookImages().stream().allMatch( local ->
                    facebookPostImages.stream().anyMatch(fb -> local.getSrc().equals(fb.getSrc()))
            );
        })).toList();
    }
}
