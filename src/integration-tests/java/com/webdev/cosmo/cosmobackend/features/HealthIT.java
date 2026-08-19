package com.webdev.cosmo.cosmobackend.features;

import com.webdev.cosmo.cosmobackend.BaseTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class HealthIT extends BaseTestConfiguration {

    @Test
    void actuatorHealthIsUpWithoutApiKey() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/actuator/health", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"UP\"");
    }

    @Test
    void everyResponseCarriesRequestIdHeader() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/actuator/health", String.class);

        assertThat(response.getHeaders().getFirst("X-Request-Id")).isNotBlank();
    }
}