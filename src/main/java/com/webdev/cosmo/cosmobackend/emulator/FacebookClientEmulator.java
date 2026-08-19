package com.webdev.cosmo.cosmobackend.emulator;

import com.webdev.cosmo.cosmobackend.service.common.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import org.openapitools.model.*;

import java.util.List;

/**
 * Drop-in replacement for the real Facebook Graph API client, active only when
 * {@code facebook.emulator.enabled=true}. Lets the app run and serve sample data
 * locally without a real page access token or network access to Facebook.
 */
public class FacebookClientEmulator implements FacebookClient {

    private static final String PAGE_ID = "cosmo_pk_page";
    private static final String EMULATED_TOKEN = "emulated-page-access-token";
    private static final String USER_TOKEN = "emulated-user-access-token";

    private final Cache cache;

    public FacebookClientEmulator(Cache cache) {
        this.cache = cache;
        ensureCache();
    }

    @Override
    public AccessTokenRs getAccessToken(String clientId, String clientSecret, String grantType) {
        AccessTokenRs rs = new AccessTokenRs();
        rs.setAccessToken(USER_TOKEN);
        rs.setTokenType("bearer");
        return rs;
    }

    @Override
    public LongLivedAccessToken getLongLivedToken(String clientId, String clientSecret,
                                                  String grantType, String fbExchangeToken) {
        LongLivedAccessToken token = new LongLivedAccessToken();
        token.setAccessToken(EMULATED_TOKEN);
        token.setTokenType("bearer");
        token.setExpiresIn(60 * 60 * 24 * 60);
        return token;
    }

    @Override
    public FacebookUser verifyToken(String userId, String accessToken, String fields) {
        FacebookUser user = new FacebookUser();
        user.setId(userId);
        user.setName("Cosmo PK");
        user.setEmail("cosmo@example.com");
        return user;
    }

    @Override
    public FacebookResponse getUserInfo(String accessToken) {
        return buildResponse(
                pageItem(),
                new FacebookDataItem().id("2").name("Another Page").accessToken(USER_TOKEN)
        );
    }

    @Override
    public FacebookResponse getPostsPage(String pageId, String accessToken, Integer limit) {
        ensureCache();
        return buildResponse(samplePosts().toArray(new FacebookDataItem[0]));
    }

    @Override
    public FacebookResponse subsequentRetrieve(String pageId, String accessToken, Integer limit, String after) {
        return buildResponse();
    }

    @Override
    public FacebookResponse getPostDetails(String postId, String accessToken) {
        return buildResponse(postDetail(postId));
    }

    @Override
    public FacebookResponse getPostAttachments(String postId, String accessToken) {
        return buildResponse();
    }

    private void ensureCache() {
        cache.setPageId(PAGE_ID);
        cache.setPageAccessToken(EMULATED_TOKEN);
    }

    private FacebookDataItem pageItem() {
        return new FacebookDataItem()
                .id(PAGE_ID)
                .name("COSMO PK")
                .accessToken(EMULATED_TOKEN);
    }

    private FacebookDataItem postDetail(String postId) {
        return new FacebookDataItem()
                .id(postId)
                .message("Sample post details for " + postId)
                .createdTime(java.time.OffsetDateTime.now());
    }

    private List<FacebookDataItem> samplePosts() {
        return List.of(
                new FacebookDataItem()
                        .id("1001")
                        .message("Witamy na stronie COSMO PK! Pierwszy przykładowy post.")
                        .createdTime(java.time.OffsetDateTime.now().minusDays(1))
                        .media(new FacebookPostMedia().image(
                                new FacebookPostImage().src("https://picsum.photos/seed/cosmo1/800/600").width(800).height(600)
                        )),
                new FacebookDataItem()
                        .id("1002")
                        .message("Drugi przykładowy post z obrazkiem.")
                        .createdTime(java.time.OffsetDateTime.now().minusDays(2))
                        .media(new FacebookPostMedia().image(
                                new FacebookPostImage().src("https://picsum.photos/seed/cosmo2/800/600").width(800).height(600)
                        )),
                new FacebookDataItem()
                        .id("1003")
                        .message("Trzeci przykładowy post — sam tekst.")
                        .createdTime(java.time.OffsetDateTime.now().minusDays(3))
        );
    }

    private FacebookResponse buildResponse(FacebookDataItem... items) {
        FacebookResponse response = new FacebookResponse();
        response.setData(List.of(items));
        return response;
    }
}
