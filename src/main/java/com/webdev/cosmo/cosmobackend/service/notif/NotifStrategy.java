package com.webdev.cosmo.cosmobackend.service.notif;

import com.webdev.cosmo.cosmobackend.service.templates.Strategy;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class NotifStrategy implements Strategy<Integer, NotifContext> {

    private final List<Consumer<NotifContext>> notifStrategyValidators;

    @Override
    public Integer run(NotifContext notifContext) {
        notifStrategyValidators.forEach(validator -> validator.accept(notifContext));

        return notifContext.getChallengeToken();
    }
}
