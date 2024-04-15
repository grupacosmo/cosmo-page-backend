package com.webdev.cosmo.cosmobackend.service.external.notif.validators;

import com.webdev.cosmo.cosmobackend.service.external.notif.NotifContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class TokenValidator implements Consumer<NotifContext> {

    private final String token;

    @Override
    public void accept(NotifContext notifContext) {
        Optional.ofNullable(notifContext)
                .map(NotifContext::getToken)
                .filter(token::equals)
                .orElseThrow(() -> new RuntimeException("Nie dziala gunwo"));
    }
}
