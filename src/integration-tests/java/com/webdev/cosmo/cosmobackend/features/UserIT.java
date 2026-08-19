package com.webdev.cosmo.cosmobackend.features;

import com.webdev.cosmo.cosmobackend.BaseTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserIT extends BaseTestConfiguration {

    private static final String API_KEY = "test-api-key";

    @Test
    void createsAndReadsUser() {
        String email = "user-" + UUID.randomUUID() + "@example.com";

        ResponseEntity<String> created = testRestTemplate.exchange(
                "/api/user",
                HttpMethod.POST,
                new HttpEntity<>(Map.of("email", email, "name", "Jan", "role", "ADMIN"), authHeaders()),
                String.class);

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> fetched = testRestTemplate.exchange(
                "/api/user/" + email,
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                String.class);

        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody()).contains(email);
    }

    @Test
    void updatesUser() {
        String email = "user-" + UUID.randomUUID() + "@example.com";

        testRestTemplate.exchange("/api/user", HttpMethod.POST,
                new HttpEntity<>(Map.of("email", email, "name", "Jan"), authHeaders()), String.class);

        ResponseEntity<String> updated = testRestTemplate.exchange("/api/user", HttpMethod.PUT,
                new HttpEntity<>(Map.of("email", email, "name", "Anna", "surname", "Nowak"), authHeaders()),
                String.class);

        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updated.getBody()).contains(email);
    }

    @Test
    void deletesUser() {
        String email = "user-" + UUID.randomUUID() + "@example.com";

        testRestTemplate.exchange("/api/user", HttpMethod.POST,
                new HttpEntity<>(Map.of("email", email), authHeaders()), String.class);

        ResponseEntity<String> deleted = testRestTemplate.exchange("/api/user/" + email, HttpMethod.DELETE,
                new HttpEntity<>(authHeaders()), String.class);

        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void rejectsRequestWithoutApiKey() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/user/missing@example.com", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", API_KEY);
        headers.set("user_id", "cosmo");
        headers.set("access_token", "emulated-user-access-token");
        return headers;
    }
}