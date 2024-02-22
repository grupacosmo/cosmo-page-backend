package com.webdev.cosmo.cosmobackend.service.notif.models;

public record NotifStrategyRecord(String token, Integer challengeToken, String subscribe) {
    public static NotifStrategyRecord init(String token, Integer challengeToken, String subscribe) {
        return new NotifStrategyRecord(token, challengeToken, subscribe);
    }
}
