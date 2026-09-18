package com.webdev.cosmo.cosmobackend.service.internal.facebook.service;

import com.webdev.cosmo.cosmobackend.service.api.Token;
import com.webdev.cosmo.cosmobackend.service.common.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.util.function.Supplier;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class StartupTokenReader implements CommandLineRunner {

    private final Supplier<Token> tokenSupplier;
    private final FacebookClient facebookClient;
    private final Cache cache;
    private final String pageTokenFromProps;
    private final String pageIdFromProps;

    @Override
    public void run(String... args) {
        Token token = tokenSupplier.get();

        if (isNull(token.getValue())) {
            token = fromProperties();
        }

        if (isNull(token.getValue())) {
            log.warn("No token stored, please provide valid one");
            return;
        }

        if (verifyToken(token)) {
            log.info("Successfully verified token. Setting up cache.");
            cache.update(token.getValue(), token.getPageId());
        } else {
            log.error("Error during token verification. Please provide valid one.");
        }
    }

    private Token fromProperties() {
        if (pageTokenFromProps == null || pageTokenFromProps.isBlank()
                || pageIdFromProps == null || pageIdFromProps.isBlank()) {
            log.warn("FB_PAGE_TOKEN and FB_PAGE_ID must both be set to use the startup fallback; skipping.");
            return new Token();
        }
        return new Token().setValue(pageTokenFromProps).setPageId(pageIdFromProps);
    }

    private boolean verifyToken(Token token) {
        try {
            facebookClient.getPostsPage(token.getPageId(), token.getValue(), 1);
            return true;
        } catch (Exception e) {
            log.warn("Token verification failed for page {}: {}", token.getPageId(), e.getMessage());
            return false;
        }
    }
}