package com.webdev.cosmo.cosmobackend.service.internal.facebook.service;

import com.webdev.cosmo.cosmobackend.service.api.Token;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Supplier;

/**
 * Provides a stored page token, falling back to an empty token (and a log line)
 * when none is present.
 */
@Slf4j
@RequiredArgsConstructor
public class TokenSupplier implements Supplier<Token> {
    private final TokenRepository tokenRepository;

    @Override
    public Token get() {
        List<Token> tokens = tokenRepository.findAll();

        if (tokens.size() > 1) {
            log.error("Multiple tokens found in the database. Using the first one.");
        }

        if (tokens.isEmpty()) {
            log.error("No tokens found in the database. Please provide a token.");
            return new Token();
        }

        return tokens.get(0);
    }
}