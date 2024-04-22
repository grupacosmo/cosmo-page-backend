package com.webdev.cosmo.cosmobackend.service.internal.facebook.service;

import com.webdev.cosmo.cosmobackend.service.api.Token;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.repository.TokenRepository;
import com.webdev.cosmo.cosmobackend.util.BetterOptional;
import com.webdev.cosmo.cosmobackend.util.interfaces.ListQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.token.TokenService;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.webdev.cosmo.cosmobackend.error.Error.NO_TOKENS_FOUND;

@Slf4j
@RequiredArgsConstructor
public class TokenSupplier implements Supplier<Token> {
    private final TokenRepository tokenRepository;

    @Override
    public Token get() {
        return BetterOptional.of(tokenRepository.findAll())
                .peek(multiTokenConsumer)
                .filter(tokens -> !tokens.isEmpty())
                .map(tokens -> {
                    System.out.println(tokens);
                    return tokens.get(0);
                })
                .get();
    }

    private Token getEmptyToken() {
        log.error("No tokens found in the database. Please provide a token.");
        return new Token();
    }

    private final Consumer<List<Token>> multiTokenConsumer = tokens -> {
        if(tokens.size() > 1) {
            log.error("Multiple tokens found in the database. Using the first one.");
        }
    };
}
