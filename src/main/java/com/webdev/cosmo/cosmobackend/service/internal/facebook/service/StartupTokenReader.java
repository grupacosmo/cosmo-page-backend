package com.webdev.cosmo.cosmobackend.service.internal.facebook.service;

import com.webdev.cosmo.cosmobackend.service.api.Token;
import com.webdev.cosmo.cosmobackend.service.external.FacebookClient;
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

    @Override
    public void run(String... args) {
        Token token = tokenSupplier.get();

        if(isNull(token.getValue())) {
           log.warn("No token stored, please provide valid one");
            return;
        }

        // fix verification based on response
        if(verifyToken(token)) {
            log.info("Successfully verified token. Setting up cache.");
            cache.setPageAccessToken(token.getValue());
            cache.setPageId(token.getPageId());
        } else {
            log.error("Error during token verification. Please provide valid one.");
        }


    }

    private boolean verifyToken(Token token) {
        try {
            var response = facebookClient.getPostsPage(token.getPageId(), token.getValue(), 1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
