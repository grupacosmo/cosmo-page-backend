package com.webdev.cosmo.cosmobackend.service.external.webhook.validators;

import com.webdev.cosmo.cosmobackend.service.external.webhook.NotifContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Consumer;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_WEBHOOK_VERIFY_TOKEN;

@RequiredArgsConstructor
public class TokenValidator implements Consumer<NotifContext> {

    private final String token;

    @Override
    public void accept(NotifContext notifContext) {
        Optional.ofNullable(notifContext)
                .map(NotifContext::getToken)
                .filter(token::equals)
                .orElseThrow(INVALID_WEBHOOK_VERIFY_TOKEN::getError);
    }
}
