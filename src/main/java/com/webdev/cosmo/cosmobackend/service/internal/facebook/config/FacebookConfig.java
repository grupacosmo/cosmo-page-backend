package com.webdev.cosmo.cosmobackend.service.internal.facebook.config;

import com.webdev.cosmo.cosmobackend.service.api.Token;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.mapper.TokenMapper;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.repository.TokenRepository;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.SaveTokenConsumer;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.StartupTokenReader;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.TokenSupplier;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import com.webdev.cosmo.cosmobackend.service.common.FacebookClient;
import org.openapitools.model.TokenModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class FacebookConfig {

    @Bean
    public Consumer<TokenModel> saveTokenConsumer(
            @Value("${facebook.client-id}") final String clientId,
            @Value("${facebook.client-secret}") final String clientSecret,
            final TokenRepository tokenRepository,
            final TokenMapper tokenMapper,
            final Cache cache,
            final FacebookClient client
    ) {
        return new SaveTokenConsumer(clientId, clientSecret, tokenRepository, tokenMapper, cache, client);
    }

    @Bean
    public Cache cache() {
        return new Cache();
    }

    @Bean
    public CommandLineRunner startupTokenReader(
            final Supplier<Token> tokenSupplier,
            final FacebookClient facebookClient,
            final Cache cache,
            @Value("${facebook.page-token:}") final String pageTokenFromProps,
            @Value("${facebook.page-id:}") final String pageIdFromProps
    ) {
        return new StartupTokenReader(tokenSupplier, facebookClient, cache, pageTokenFromProps, pageIdFromProps);
    }

    @Bean
    public Supplier<Token> tokenSupplier(
            final TokenRepository tokenRepository
    ) {
        return new TokenSupplier(tokenRepository);
    }
}
