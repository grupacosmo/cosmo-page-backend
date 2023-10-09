package com.webdev.cosmo.cosmobackend.features;

import com.webdev.cosmo.cosmobackend.BaseTestConfiguration;
import org.junit.jupiter.api.Test;

public class BookFeatureIT extends BaseTestConfiguration {

    @Test
    void sampleIntegrationTest() {
        var a = testRestTemplate.getForEntity("/api/books", String.class);
    }
}
