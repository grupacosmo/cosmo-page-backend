package com.webdev.cosmo.cosmobackend.emulator;

import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import org.junit.jupiter.api.Test;
import org.openapitools.model.FacebookResponse;

import static org.assertj.core.api.Assertions.assertThat;

class FacebookClientEmulatorTest {

    private final Cache cache = new Cache();
    private final FacebookClientEmulator emulator = new FacebookClientEmulator(cache);

    @Test
    void constructorPopulatesCache() {
        assertThat(cache.getPageAccessToken()).isEqualTo("emulated-page-access-token");
        assertThat(cache.getPageId()).isEqualTo("cosmo_pk_page");
    }

    @Test
    void getPostsPageReturnsSampleData() {
        FacebookResponse response = emulator.getPostsPage("any", "any", 10);

        assertThat(response.getData()).isNotEmpty();
        assertThat(response.getData())
                .anyMatch(item -> item.getMessage() != null && !item.getMessage().isBlank());
    }

    @Test
    void getUserInfoContainsCosmoPage() {
        FacebookResponse response = emulator.getUserInfo("any");

        assertThat(response.getData())
                .anyMatch(item -> "COSMO PK".equals(item.getName()));
    }

    @Test
    void getLongLivedTokenReturnsEmulatedToken() {
        var token = emulator.getLongLivedToken("id", "secret", "grant", "exchange");

        assertThat(token.getAccessToken()).isEqualTo("emulated-page-access-token");
        assertThat(token.getExpiresIn()).isPositive();
    }

    @Test
    void getPostAttachmentsReturnsMediaForSamplePostsWithImages() {
        FacebookResponse response = emulator.getPostAttachments("1001", "any");

        assertThat(response.getData()).hasSize(1);
        assertThat(response.getData().get(0).getMedia().getImage().getSrc()).isNotBlank();
    }

    @Test
    void getPostAttachmentsReturnsNoDataForTextOnlyOrUnknownPosts() {
        assertThat(emulator.getPostAttachments("1003", "any").getData()).isEmpty();
        assertThat(emulator.getPostAttachments("unknown", "any").getData()).isEmpty();
    }
}