package com.webdev.cosmo.cosmobackend.features;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webdev.cosmo.cosmobackend.BaseTestConfiguration;
import com.webdev.cosmo.cosmobackend.contract.OpenApiContract;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ContractsIT extends BaseTestConfiguration {

    private static final String API_KEY = "test-api-key";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void paginatedPostsListMatchesOpenApiContract() {
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/posts?page=0&size=5",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        OpenApiContract.assertValid("PostPage", response.getBody());
    }

    @Test
    void postDetailsMatchOpenApiContract() throws Exception {
        ResponseEntity<String> list = testRestTemplate.exchange(
                "/api/posts?page=0&size=5",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                String.class);

        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode content = MAPPER.readTree(list.getBody()).get("content");
        assertThat(content).hasSizeGreaterThan(0);
        String postId = content.get(0).get("id").asText();

        ResponseEntity<String> details = testRestTemplate.exchange(
                "/api/posts/" + postId,
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                String.class);

        assertThat(details.getStatusCode()).isEqualTo(HttpStatus.OK);
        OpenApiContract.assertValid("PostListQueryItemDetails", details.getBody());
    }

    private HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", API_KEY);
        return headers;
    }
}