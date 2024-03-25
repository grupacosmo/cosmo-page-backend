package com.webdev.cosmo.cosmobackend.security;

import com.webdev.cosmo.cosmobackend.service.external.FacebookClient;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.FacebookUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final FacebookClient facebookClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        FacebookAuthentication facebookAuthentication = (FacebookAuthentication) authentication;

        try {
            FacebookUser facebookResponse = facebookClient.verifyToken((String) facebookAuthentication.getPrincipal(), (String) facebookAuthentication.getCredentials(), "id,name,email,picture");
            facebookAuthentication.setAuthenticated(true);
            facebookAuthentication.setEmail(facebookResponse.getEmail());
        } catch (FeignException e) {
            log.error(e.getMessage());
            facebookAuthentication.setAuthenticated(false);
        }

        return facebookAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return FacebookAuthentication.class.equals(authentication);
    }
}
