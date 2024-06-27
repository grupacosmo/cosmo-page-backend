package com.webdev.cosmo.cosmobackend.service.external.webhook.config;

import com.webdev.cosmo.cosmobackend.service.external.webhook.NotifContext;
import com.webdev.cosmo.cosmobackend.service.external.webhook.NotifStrategy;
import com.webdev.cosmo.cosmobackend.service.external.webhook.Strategy;
import com.webdev.cosmo.cosmobackend.service.external.webhook.validators.TokenValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.function.Consumer;

@Configuration
public class BeansConfig {
    @Bean("notifContext")
    @RequestScope
    public NotifContext notifContext() {
        return new NotifContext();
    }

    @Bean("notifStrategy")
    public Strategy<Integer, NotifContext> notifStrategy(
            @Qualifier("notifValidators") final List<Consumer<NotifContext>> notifValidators
    ) {
        return new NotifStrategy(notifValidators);
    }

    @Bean("notifValidators")
    public List<Consumer<NotifContext>> notifValidators(
            @Value("${facebook.notif-token}") final String tokenFromProps
    ) {
        return List.of(
                new TokenValidator(tokenFromProps)
        );
    }
}
