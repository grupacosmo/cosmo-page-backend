package com.webdev.cosmo.cosmobackend.features;

import com.webdev.cosmo.cosmobackend.BaseTestConfiguration;
import com.webdev.cosmo.cosmobackend.factory.FacebookNotifFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FacebookNotifIT extends BaseTestConfiguration {

    @Test
    void testSampleNotifHandshake() {
        final Integer challengeToken = 2137;
        final String url = FacebookNotifFactory.buildUrl(challengeToken);
        final Integer actualToken = testRestTemplate.getForObject(url, Integer.class);

        assertThat(challengeToken).isEqualTo(actualToken);
    }
}
