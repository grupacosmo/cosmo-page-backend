package com.webdev.cosmo.cosmobackend.security;

import com.webdev.cosmo.cosmobackend.security.mapper.FacebookAuthenticationMapper;
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
    private final FacebookAuthenticationMapper facebookAuthenticationMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {
            FacebookUser facebookResponse = facebookClient.verifyToken((String) authentication.getPrincipal(), (String) authentication.getCredentials(), "id,name,email,picture");
            return facebookAuthenticationMapper.map(facebookResponse);
        } catch (FeignException e) {
            log.error(e.getMessage());
            authentication.setAuthenticated(false);
        }

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return FacebookAuthentication.class.equals(authentication);
    }
}
