package com.webdev.cosmo.cosmobackend.security.predicate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Predicate;

@Component
public class ApiKeyValidator implements Predicate<String> {

    private final String allowedApiKeys;

    public ApiKeyValidator(@Value("${env.api-keys}") String allowedApiKeys) {
        this.allowedApiKeys = allowedApiKeys;
    }

    @Override
    public boolean test(String key) {
        return Arrays.asList(allowedApiKeys.split(",")).contains(key);
    }

}
