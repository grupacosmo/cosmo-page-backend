package com.webdev.cosmo.cosmobackend.features;

import com.github.javafaker.Faker;
import com.webdev.cosmo.cosmobackend.BaseTestConfiguration;
import com.webdev.cosmo.cosmobackend.factory.FacebookNotifFactory;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageIT extends BaseTestConfiguration {

    @Test
    void testSampleNotifHandshake() {
        Resource fileResource = new ByteArrayResource("This is a test image content".getBytes()) {
            @Override
            public String getFilename() {
                return "test.jpg";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("images", fileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/images",
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
