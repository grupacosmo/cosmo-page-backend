package com.webdev.cosmo.cosmobackend.service.external.webhook;

import com.webdev.cosmo.cosmobackend.service.common.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Change;
import org.openapitools.model.Entry;
import org.openapitools.model.FacebookDataItem;
import org.openapitools.model.FacebookResponse;
import org.openapitools.model.Value;
import org.openapitools.model.WebhookNotification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WebhookNotificationConsumerTest {

    private final FacebookClient facebookClient = mock(FacebookClient.class);
    private final PostMapper postMapper = mock(PostMapper.class);
    private final PostRepository postRepository = mock(PostRepository.class);
    private final Cache cache = new Cache();

    @Test
    void throwsWhenNotificationCarriesNoPostId() {
        WebhookNotificationConsumer consumer = new WebhookNotificationConsumer(facebookClient, postMapper, postRepository, cache);

        assertThatCode(() -> consumer.accept(new WebhookNotification().entry(List.of())))
                .isInstanceOf(com.webdev.cosmo.cosmobackend.error.ServiceError.class);
    }

    @Test
    void nullNotificationDoesNotThrowBeforeReported() {
        WebhookNotificationConsumer consumer = new WebhookNotificationConsumer(facebookClient, postMapper, postRepository, cache);

        assertThatCode(() -> consumer.accept(null))
                .isInstanceOf(com.webdev.cosmo.cosmobackend.error.ServiceError.class);
    }

    @Test
    void skipsSavingWhenPostDetailsCarryNoData() {
        when(facebookClient.getPostDetails("1001", cache.getPageAccessToken()))
                .thenReturn(new FacebookResponse().data(List.of()));

        WebhookNotificationConsumer consumer = new WebhookNotificationConsumer(facebookClient, postMapper, postRepository, cache);
        WebhookNotification notification = notificationForPost("1001");

        assertThatCode(() -> consumer.accept(notification)).doesNotThrowAnyException();
        verify(postRepository, never()).save(any());
    }

    @Test
    void savesTextOnlyPostWithoutAttachments() {
        when(facebookClient.getPostDetails("1001", cache.getPageAccessToken()))
                .thenReturn(new FacebookResponse().data(List.of(new FacebookDataItem().id("1001").message("Nowy wpis"))));
        when(facebookClient.getPostAttachments("1001", cache.getPageAccessToken()))
                .thenReturn(new FacebookResponse().data(List.of()));

        WebhookNotificationConsumer consumer = new WebhookNotificationConsumer(facebookClient, postMapper, postRepository, cache);
        WebhookNotification notification = notificationForPost("1001");

        assertThatCode(() -> consumer.accept(notification)).doesNotThrowAnyException();
        verify(postRepository).save(any());
    }

    private WebhookNotification notificationForPost(String postId) {
        return new WebhookNotification().entry(List.of(
                new Entry().changes(List.of(
                        new Change().value(new Value().postId(postId))))));
    }
}