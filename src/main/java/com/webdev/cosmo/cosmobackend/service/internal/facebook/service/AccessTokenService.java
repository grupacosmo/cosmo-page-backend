package com.webdev.cosmo.cosmobackend.service.internal.facebook.service;

import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.external.FacebookApiClent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.AccessTokenRs;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenService {

    @Value("${facebook.client-id}")
    private final String clientId;

    @Value("${facebook.client-secret}")
    private final String clientSecret;

    private final FacebookApiClent facebookApiClent;

    // Cacheable value
    private String accessToken;

    public String getAccessToken() {
        return Optional.ofNullable(accessToken)
                .orElse(refreshToken());
    }

    public String refreshToken() {
        AccessTokenRs tokenResponse = facebookApiClent.getAccessToken(clientId, clientSecret, "client_credentials");

        String token = Optional.ofNullable(tokenResponse)
                .map(AccessTokenRs::getAccessToken)
                .orElseThrow(() -> new RuntimeException("Cannot get token due to provider issues"));

        log.info("Successfully obtained token from provider: {}", token);

        this.accessToken = token;
        return token;
    }

}
