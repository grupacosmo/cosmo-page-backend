package com.webdev.cosmo.cosmobackend.service.internal.posts.service.builder;

import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.model.FacebookDataItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
public class UnsavedPostsExtractor implements BiFunction<List<FacebookDataItem>, List<Post>, List<FacebookDataItem>> {

    @Override
    public List<FacebookDataItem> apply(List<FacebookDataItem> facebookDataItems, List<Post> posts) {
        return facebookDataItems.stream()
                .filter(item -> posts.stream().noneMatch(post -> StringUtils.equals(item.getId(), post.getId())))
                .toList();
    }
}
