package com.webdev.cosmo.cosmobackend.config;

import com.webdev.cosmo.cosmobackend.config.properties.EndpointConfig;
import com.webdev.cosmo.cosmobackend.security.CustomAuthenticationFilter;
import com.webdev.cosmo.cosmobackend.security.CustomAuthenticationManager;
import com.webdev.cosmo.cosmobackend.security.CustomAuthenticationProvider;
import com.webdev.cosmo.cosmobackend.security.mapper.FacebookAuthenticationMapper;
import com.webdev.cosmo.cosmobackend.service.external.FacebookClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    AuthenticationProvider authenticationProvider(final FacebookClient facebookClient, final FacebookAuthenticationMapper facebookAuthenticationMapper) {
        return new CustomAuthenticationProvider(facebookClient, facebookAuthenticationMapper);
    }

    @Bean
    OncePerRequestFilter customAuthenticationFilter(final EndpointConfig endpointConfig, final AuthenticationManager customAuthenticationManager) {
        return new CustomAuthenticationFilter(endpointConfig, customAuthenticationManager);
    }

    @Bean
    AuthenticationManager customAuthenticationManager(final AuthenticationProvider authenticationProvider) {
        return new CustomAuthenticationManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, OncePerRequestFilter customAuthenticationFilter, EndpointConfig endpointConfig) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(endpointConfig.getSecured().toArray(String[]::new)).authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(LogoutConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterAfter(customAuthenticationFilter, BasicAuthenticationFilter.class);

        return http.build();
    }
}
