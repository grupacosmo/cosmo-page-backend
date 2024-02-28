package com.webdev.cosmo.cosmobackend.service.external.facebook.async;

import com.webdev.cosmo.cosmobackend.service.external.facebook.external.FacebookAccessTokenClient;
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

    private final FacebookAccessTokenClient facebookAccessTokenClient;

    // Cacheable value
    private String accessToken;

    public String getAccessToken() {
        return Optional.ofNullable(accessToken)
                .orElse(refreshToken());
    }

    public String refreshToken() {
        AccessTokenRs tokenResponse = facebookAccessTokenClient.getAccessToken(clientId, clientSecret, "client_credentials");

        String token = Optional.ofNullable(tokenResponse)
                .map(AccessTokenRs::getAccessToken)
                .orElseThrow(() -> new RuntimeException("Cannot get token due to provider issues"));

        log.info("Successfully obtained token from provider: {}", token);

        this.accessToken = token;
        return token;
    }

}
