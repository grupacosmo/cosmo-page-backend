package com.webdev.cosmo.cosmobackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OncePerRequestFilter customAuthenticationFilter;

    @Value("${endpoint.secured}")
    private String[] securedEndpoints;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                    .requestMatchers(securedEndpoints).authenticated()
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
