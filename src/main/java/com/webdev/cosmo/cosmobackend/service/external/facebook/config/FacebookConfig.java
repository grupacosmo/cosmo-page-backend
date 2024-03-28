package com.webdev.cosmo.cosmobackend.service.external.facebook.config;

import com.webdev.cosmo.cosmobackend.service.external.facebook.async.AccessTokenService;
import com.webdev.cosmo.cosmobackend.service.external.facebook.external.FacebookAccessTokenClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacebookConfig {

    @Bean
    public AccessTokenService accessTokenService(
            @Value("${facebook.client-id}") final String clientId,
            @Value("${facebook.client-secret}") final String clientSecret,
            final FacebookAccessTokenClient facebookAccessTokenClient
            ) {
        return new AccessTokenService(clientId, clientSecret, facebookAccessTokenClient);
    }
}
