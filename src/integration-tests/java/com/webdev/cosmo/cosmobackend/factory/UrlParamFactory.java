package com.webdev.cosmo.cosmobackend.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlParamFactory {

    public static String buildParams(final Map<String, String> params) {
        return "?".concat(params.entrySet().stream()
                .map(entry -> "%s=%s&".formatted(entry.getKey(), entry.getValue()))
                .collect(Collectors.joining()));
    }
}
