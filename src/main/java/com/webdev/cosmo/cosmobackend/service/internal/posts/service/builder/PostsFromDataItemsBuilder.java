package com.webdev.cosmo.cosmobackend.service.internal.posts.service.builder;

import com.webdev.cosmo.cosmobackend.service.external.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.openapitools.model.FacebookDataItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PostsFromDataItemsBuilder implements Function<List<FacebookDataItem>, List<Post>> {

    private final FacebookClient facebookClient;
    private final Cache cache;
    private final PostMapper postMapper;

    @Override
    public List<Post> apply(List<FacebookDataItem> facebookDataItems) {
        return facebookDataItems.stream()
                .map(facebookDataItem -> Pair.of(facebookDataItem, facebookClient.getPostAttachments(facebookDataItem.getId(), cache.getPageAccessToken())))
                .map(postMapper::mapPostFromFacebookData)
                .toList();
    }
}
