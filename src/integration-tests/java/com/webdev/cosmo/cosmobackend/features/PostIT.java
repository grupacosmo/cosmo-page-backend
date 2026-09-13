package com.webdev.cosmo.cosmobackend.features;

import com.webdev.cosmo.cosmobackend.BaseTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class PostIT extends BaseTestConfiguration {

    private static final String API_KEY = "test-api-key";

    @Test
    void returnsPostsWithPagingParams() {
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/posts?page=0&size=5",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void rejectsRequestWithoutPagingParams() {
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/posts",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void rejectsRequestWithoutApiKey() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/posts", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void missingPostReturnsNotFound() {
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/posts/does-not-exist",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void syncsSamplePostsFromEmulator() {
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/posts/sync",
                HttpMethod.PUT,
                new HttpEntity<>(authHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    private HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", API_KEY);
        return headers;
    }
}