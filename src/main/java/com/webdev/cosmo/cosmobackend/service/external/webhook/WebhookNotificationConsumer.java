package com.webdev.cosmo.cosmobackend.service.external.webhook;

import com.webdev.cosmo.cosmobackend.service.common.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.openapitools.model.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.function.Consumer;

import static com.webdev.cosmo.cosmobackend.error.Error.WEBHOOK_NOT_SUPPORTED;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookNotificationConsumer implements Consumer<WebhookNotification> {

    private final FacebookClient facebookClient;
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final Cache cache;

    @Override
    public void accept(WebhookNotification webhookNotification) {
        String postId = webhookNotification.getEntry().stream()
                .findFirst()
                .map(Entry::getChanges)
                .orElse(Collections.emptyList())
                .stream()
                .map(Change::getValue)
                .map(Value::getFrom)
                .map(From::getId)
                .findFirst()
                .orElseThrow(WEBHOOK_NOT_SUPPORTED::getError);

        var fbPost = facebookClient.getPostAttachments(postId, cache.getPageAccessToken());
        log.info("Post received after webhook notification: " + fbPost.toString());
        var mappedPost = postMapper.mapPostFromFacebookData(fbPost.getData().get(0));

        postRepository.save(mappedPost);
        log.info("Successfully saved post" + mappedPost);
    }
}
