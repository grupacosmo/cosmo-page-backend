package com.webdev.cosmo.cosmobackend.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FacebookNotifFactory {
    private static final String NOTIF_URL = "/facebook/notif";

    public static String buildUrl(final Integer challengeToken) {
        return NOTIF_URL.concat(UrlParamFactory.buildParams(Map.of(
                "hub.mode", "subscribe",
                "hub.verify_token", "dupa123",
                "hub.challenge", String.valueOf(challengeToken)
                )));
    }
}
