package com.webdev.cosmo.cosmobackend.security;

import com.webdev.cosmo.cosmobackend.config.EndpointConfig;
import com.webdev.cosmo.cosmobackend.security.filters.UserAuthenticationFilter;
import com.webdev.cosmo.cosmobackend.security.mapper.FacebookAuthenticationMapper;
import com.webdev.cosmo.cosmobackend.service.common.FacebookClient;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    AuthenticationProvider authenticationProvider(final FacebookClient facebookClient, final FacebookAuthenticationMapper facebookAuthenticationMapper) {
        return new CustomAuthenticationProvider(facebookClient, facebookAuthenticationMapper);
    }

    @Bean
    OncePerRequestFilter customAuthenticationFilter(final EndpointConfig endpointConfig, final AuthenticationManager customAuthenticationManager) {
        return new UserAuthenticationFilter(endpointConfig, customAuthenticationManager);
    }

    @Bean
    AuthenticationManager customAuthenticationManager(final AuthenticationProvider authenticationProvider) {
        return new CustomAuthenticationManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           OncePerRequestFilter customAuthenticationFilter,
                                           EndpointConfig endpointConfig) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(endpointConfig.getSecured().toArray(String[]::new)).authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(LogoutConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterAfter(customAuthenticationFilter, BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("localhost", "cosmopk.pl"));
        configuration.setAllowedMethods(List.of("POST", "PUT", "GET"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }
}
