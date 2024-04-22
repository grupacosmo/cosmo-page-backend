package com.webdev.cosmo.cosmobackend.service.internal.facebook.service;

import com.webdev.cosmo.cosmobackend.service.api.Token;
import com.webdev.cosmo.cosmobackend.service.external.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class StartupTokenReader implements CommandLineRunner {

    private final Supplier<Token> tokenSupplier;
    private final FacebookClient facebookClient;
    private final Cache cache;

    @Override
    public void run(String... args) {
        Token token = tokenSupplier.get();

        // fix verification based on response
        verifyToken(token);

        log.info("Successfully verified token. Setting up cache.");
        cache.setPageAccessToken(token.getValue());
        cache.setPageId(token.getPageId());
    }

    private void verifyToken(Token token) {
        try {
            facebookClient.getPostsPage(token.getPageId(), token.getValue());
        } catch (Exception e) {
            log.error("Invalid token. Please provide a valid token.");
            // send mail here
        }
    }
}
