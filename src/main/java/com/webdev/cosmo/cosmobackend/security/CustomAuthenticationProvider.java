package com.webdev.cosmo.cosmobackend.security;

import com.webdev.cosmo.cosmobackend.service.external.FacebookClient;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final FacebookClient facebookClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        FacebookAuthentication facebookAuthentication = (FacebookAuthentication) authentication;
        FacebookUser principal = (FacebookUser)facebookAuthentication.getPrincipal();

        try {
            FacebookUser facebookResponse = facebookClient.verifyToken(principal.getUserId(), principal.getAccessToken(), "id,name,email,picture");
            facebookAuthentication.setAuthenticated(true);
            facebookAuthentication.setFacebookUser(facebookResponse);
        }catch(FeignException e){
            log.error(e.getMessage());
        }

        return facebookAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return FacebookAuthentication.class.equals(authentication);
    }
}
