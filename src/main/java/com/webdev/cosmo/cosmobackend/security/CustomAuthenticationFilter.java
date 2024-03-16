package com.webdev.cosmo.cosmobackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private static final Set<String> securedEndpoints = Set.of("/user");

    private final AuthenticationManager customAuthenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String userId = request.getHeader("user_id");
        final String accessToken = request.getHeader("access_token");

        String requestURI = request.getRequestURI();


        if(isPermitAllEndpoint(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        if(isNull(accessToken) || isNull(userId)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        FacebookAuthentication facebookAuthentication = new FacebookAuthentication(userId, accessToken);
        Authentication authenticatedUser = customAuthenticationManager.authenticate(facebookAuthentication);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        filterChain.doFilter(request, response);

    }

    private boolean isPermitAllEndpoint(String requestURI){
        return securedEndpoints.stream().noneMatch(requestURI::startsWith);
    }

}
