package com.webdev.cosmo.cosmobackend.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webdev.cosmo.cosmobackend.config.properties.EndpointConfig;
import com.webdev.cosmo.cosmobackend.error.Error;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

import static java.util.Objects.isNull;



@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final EndpointConfig endpointConfig;

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
            response.setContentType("application/json");
            Error errorResponse = Error.NO_ACCESS_TOKEN_OR_USER_ID;

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Error.class, new Error.Serializer())
                    .create();

            String jsonErrorResponse = gson.toJson(errorResponse);

            PrintWriter writer = response.getWriter();
            writer.print(jsonErrorResponse);
            writer.flush();
            return;
        }

        FacebookAuthentication facebookAuthentication = new FacebookAuthentication(userId, accessToken);
        Authentication authenticatedUser = customAuthenticationManager.authenticate(facebookAuthentication);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        filterChain.doFilter(request, response);

    }

    private boolean isPermitAllEndpoint(String requestURI){
       return  endpointConfig.getSecured().stream().noneMatch(requestURI::startsWith);
    }

}
