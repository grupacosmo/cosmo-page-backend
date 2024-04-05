package com.webdev.cosmo.cosmobackend.service.internal.facebook.config;

import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.AccessTokenService;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.external.FacebookApiClent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacebookConfig {

    @Bean
    public AccessTokenService accessTokenService(
            @Value("${facebook.client-id}") final String clientId,
            @Value("${facebook.client-secret}") final String clientSecret,
            final FacebookApiClent facebookApiClent
            ) {
        return new AccessTokenService(clientId, clientSecret, facebookApiClent);
    }
}
