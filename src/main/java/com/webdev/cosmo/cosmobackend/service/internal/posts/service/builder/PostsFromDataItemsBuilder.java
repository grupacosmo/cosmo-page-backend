package com.webdev.cosmo.cosmobackend.service.internal.posts.service.builder;

import com.webdev.cosmo.cosmobackend.service.common.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.openapitools.model.FacebookDataItem;
import org.openapitools.model.FacebookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PostsFromDataItemsBuilder implements Function<List<FacebookDataItem>, List<Post>> {

    private final FacebookClient facebookClient;
    private final Cache cache;
    private final PostMapper postMapper;

    @Autowired
    protected PostRepository postRepository;

    @Override
    public List<Post> apply(List<FacebookDataItem> facebookDataItems) {
        return facebookDataItems.stream()
                .map(facebookDataItem -> {
                    FacebookResponse attachments = facebookClient.getPostAttachments(
                            facebookDataItem.getId(),
                            cache.getPageAccessToken()
                    );

                    Post post = postRepository.findByProviderId(facebookDataItem.getId())
                            .orElseGet(() -> postMapper.mapPostFromFacebookData(facebookDataItem));

                    return Pair.of(Pair.of(facebookDataItem, attachments), post);
                })
                .map(pair -> postMapper.mapPostFromFacebookData(pair.getLeft(), pair.getRight()))
                .toList();

    }
}
