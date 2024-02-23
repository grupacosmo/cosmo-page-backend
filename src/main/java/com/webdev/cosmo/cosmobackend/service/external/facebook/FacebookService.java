package com.webdev.cosmo.cosmobackend.service.external.facebook;

import com.webdev.cosmo.cosmobackend.service.external.facebook.async.AccessTokenService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FacebookService {
    private final WebClient webClient;
    private final AccessTokenService accessTokenService;

    public FacebookService(WebClient.Builder webClientBuilder, AccessTokenService accessTokenService) {
        this.webClient = webClientBuilder.baseUrl("https://graph.facebook.com").build();
        this.accessTokenService = accessTokenService;
    }

    public Mono<String> getPagePosts(String pageId) {
        String token = accessTokenService.getAccessToken();

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{pageId}/posts")
                        .queryParam("access_token", token)
                        .build(pageId))
                .retrieve()
                .bodyToMono(String.class);
    }
}
