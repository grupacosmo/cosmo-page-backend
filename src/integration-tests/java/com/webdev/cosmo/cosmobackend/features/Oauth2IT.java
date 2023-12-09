package com.webdev.cosmo.cosmobackend.features;

import com.webdev.cosmo.cosmobackend.BaseTestConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import static org.mockito.Mockito.when;

public class Oauth2IT extends BaseTestConfiguration {


    @MockBean
    JwtAuthenticationProvider authenticationProvider;

    @Test
    void testOAuth2FailWhenAccessSecuredEndpointWithoutToken() {
        var response = testRestTemplate.getForEntity("/api/verification/secured", String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(401));
        Assertions.assertNull(response.getBody());
    }


    @Test
    void testOAuth2SuccessWhenAccessingUnsecuredEndpointWithoutToken() {
        var response = testRestTemplate.getForEntity("/api/verification/unsecured", String.class);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(200));
    }

    @Test
    void testOAuth2CorrectToken() {

        HttpHeaders headers = new HttpHeaders();
        String idToken = "Bearer token";
        BearerTokenAuthenticationToken authenticationToken = new BearerTokenAuthenticationToken(idToken);
        authenticationToken.setAuthenticated(true);

        when(authenticationProvider.authenticate(Mockito.any(Authentication.class))).thenReturn(authenticationToken);
        headers.add("Authorization", idToken);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/verification/secured", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(200));

    }

    @Test
    void testOAuth2WrongToken() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer wrong token");
        ResponseEntity<String> response = testRestTemplate.exchange("/api/verification/secured", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertNull(response.getBody());
        Assertions.assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(401));

    }


}
