package com.webdev.cosmo.cosmobackend.security.filters;

import com.webdev.cosmo.cosmobackend.config.EndpointConfig;
import com.webdev.cosmo.cosmobackend.error.Error;
import com.webdev.cosmo.cosmobackend.error.ErrorResponseWriter;
import com.webdev.cosmo.cosmobackend.security.FacebookAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.isNull;


@RequiredArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final EndpointConfig endpointConfig;
    private final AuthenticationManager customAuthenticationManager;
    private final ErrorResponseWriter errorResponseWriter;

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
            errorResponseWriter.write(response, Error.NO_ACCESS_TOKEN_OR_USER_ID);
            return;
        }

        FacebookAuthentication facebookAuthentication = new FacebookAuthentication(userId, accessToken);
        Authentication authenticatedUser = customAuthenticationManager.authenticate(facebookAuthentication);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        filterChain.doFilter(request, response);

    }

    private boolean isPermitAllEndpoint(String requestURI){
       return  endpointConfig.getSecured().stream().noneMatch(pattern -> PATH_MATCHER.match(pattern, requestURI));
    }

}