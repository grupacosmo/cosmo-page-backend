package com.webdev.cosmo.cosmobackend.service.external.webhook;

import com.webdev.cosmo.cosmobackend.service.common.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.openapitools.model.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

import static com.webdev.cosmo.cosmobackend.error.Error.WEBHOOK_NOT_SUPPORTED;

/**
 * Handles a Facebook webhook notification by fetching the changed post from the
 * Graph API and saving it locally.
 */
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
        String postId = Optional.ofNullable(webhookNotification)
                .map(WebhookNotification::getEntry)
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .map(Entry::getChanges)
                .orElse(Collections.emptyList())
                .stream()
                .map(Change::getValue)
                .map(Value::getPostId)
                .findFirst()
                .orElseThrow(WEBHOOK_NOT_SUPPORTED::getError);

        FacebookResponse fbPostDetails = facebookClient.getPostDetails(postId, cache.getPageAccessToken());

        if (fbPostDetails.getData() == null || fbPostDetails.getData().isEmpty()) {
            log.warn("Webhook notification carried no post details; nothing to save.");
            return;
        }

        FacebookDataItem postData = fbPostDetails.getData().get(0);
        FacebookResponse attachments = facebookClient.getPostAttachments(postId, cache.getPageAccessToken());

        Post post = postRepository.findByProviderId(postData.getId())
                .orElseGet(() -> postMapper.mapPostFromFacebookData(postData));
        post = postMapper.mapPostFromFacebookData(Pair.of(postData, attachments), post);

        log.info("Post received after webhook notification.");
        postRepository.save(post);
        log.info("Successfully saved post after webhook notification.");
    }
}
