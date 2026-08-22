package com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async;

import lombok.Data;

/**
 * Shared in-memory holder for the current page access token and page id.
 * Mutable singleton read/written across request threads and background jobs.
 */
@Data
public class Cache {
    private volatile String pageAccessToken;
    private volatile String pageId;
}