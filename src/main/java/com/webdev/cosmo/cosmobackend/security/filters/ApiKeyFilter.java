package com.webdev.cosmo.cosmobackend.security.filters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webdev.cosmo.cosmobackend.error.Error;
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
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final Predicate<String> apiKeyValidator;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!apiKeysRequired || !StringUtils.hasText(apiKeys) || PATHS_TO_BE_SKIPPED.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String apiKey = request.getHeader("apiKey");

        if(!PATHS_TO_BE_SKIPPED.contains(request.getRequestURI()) && apiKeyValidator.negate().test(apiKey)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            Error errorResponse = Error.INVALID_API_KEY;

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Error.class, new Error.Serializer())
                    .create();

            String jsonErrorResponse = gson.toJson(errorResponse);

            PrintWriter writer = response.getWriter();
            writer.print(jsonErrorResponse);
            writer.flush();
            return;
        }

        filterChain.doFilter(request, response);
    }

}
