package com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async;

/**
 * Shared in-memory holder for the current page access token and page id.
 * Both values are kept in a single immutable snapshot so concurrent readers
 * never observe a token/page-id pair coming from two different updates.
 */
public class Cache {

    private volatile TokenState state = TokenState.EMPTY;

    public String getPageAccessToken() {
        return state.pageAccessToken;
    }

    public String getPageId() {
        return state.pageId;
    }

    public void setPageAccessToken(String pageAccessToken) {
        this.state = state.withPageAccessToken(pageAccessToken);
    }

    public void setPageId(String pageId) {
        this.state = state.withPageId(pageId);
    }

    public void update(String pageAccessToken, String pageId) {
        this.state = new TokenState(pageAccessToken, pageId);
    }

    private record TokenState(String pageAccessToken, String pageId) {

        private static final TokenState EMPTY = new TokenState(null, null);

        private TokenState withPageAccessToken(String pageAccessToken) {
            return new TokenState(pageAccessToken, pageId);
        }

        private TokenState withPageId(String pageId) {
            return new TokenState(pageAccessToken, pageId);
        }
    }
}