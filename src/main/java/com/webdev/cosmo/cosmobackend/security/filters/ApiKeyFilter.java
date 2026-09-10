package com.webdev.cosmo.cosmobackend.security.filters;

import com.webdev.cosmo.cosmobackend.error.Error;
import com.webdev.cosmo.cosmobackend.error.ErrorResponseWriter;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final Predicate<String> apiKeyValidator;
    private final ErrorResponseWriter errorResponseWriter;

    @Value("${env.api-keys:}")
    private String apiKeys;

    @Value("${env.api-keys-required:false}")
    private boolean apiKeysRequired;

    private static final List<String> PATHS_TO_BE_SKIPPED = List.of(
            "/api/facebook/notif",
            "/api/user-privacy/terms",
            "/api/user-privacy/policy",
            "/actuator/health"
    );

    @PostConstruct
    void validateConfiguration() {
        if (apiKeysRequired && !StringUtils.hasText(apiKeys)) {
            throw new IllegalStateException(
                    "API keys are required (api-keys-required=true) but no API_KEYS are configured. "
                            + "The API would fail open without a configured key list.");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (PATHS_TO_BE_SKIPPED.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!apiKeysRequired) {
            filterChain.doFilter(request, response);
            return;
        }

        final String apiKey = request.getHeader("apiKey");

        if (!StringUtils.hasText(apiKeys) || apiKeyValidator.negate().test(apiKey)) {
            errorResponseWriter.write(response, Error.INVALID_API_KEY);
            return;
        }

        filterChain.doFilter(request, response);
    }

}