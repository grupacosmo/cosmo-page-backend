package com.webdev.cosmo.cosmobackend.service.internal.facebook.config;

import com.webdev.cosmo.cosmobackend.service.api.Token;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.mapper.TokenMapper;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.repository.TokenRepository;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.SaveTokenConsumer;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.StartupTokenReader;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.TokenSupplier;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.AccessTokenRefreshJob;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import com.webdev.cosmo.cosmobackend.service.common.FacebookClient;
import org.openapitools.model.TokenModel;
import org.quartz.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class FacebookConfig {

    public static final String ACCESS_TOKEN_REFRESH_JOB = "AccessTokenRefreshJob";
    public static final String ACCESS_TOKEN_REFRESH_TRIGGER = "AccessTokenRefreshTrigger";

//    @Bean
//    public QuartzJobBean accessTokenRefreshJob(
//            final PageTokenService pageTokenService
//    ) {
//        return new AccessTokenRefreshJob(pageTokenService);
//    }
//
//    @Bean
//    public PageTokenService accessTokenService(
//            @Value("${facebook.client-id}") final String clientId,
//            @Value("${facebook.client-secret}") final String clientSecret,
//            final FacebookClient facebookClient
//            ) {
//        return new PageTokenService(clientId, clientSecret, facebookClient);
//    }

    @Bean
    public JobDetail accessTokenRefreshJobDetail() {
        return JobBuilder.newJob(AccessTokenRefreshJob.class)
                .withIdentity(ACCESS_TOKEN_REFRESH_JOB)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger accessTokenRefreshTrigger(JobDetail accessTokenRefreshJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(accessTokenRefreshJobDetail)
                .withIdentity(ACCESS_TOKEN_REFRESH_TRIGGER)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .repeatForever()
                        .withIntervalInHours(10)
                )
                .build();
    }

    @Bean
    public Consumer<TokenModel> saveTokenConsumer(
            final TokenRepository tokenRepository,
            final TokenMapper tokenMapper,
            final Cache cache,
            final FacebookClient client
    ) {
        return new SaveTokenConsumer(tokenRepository, tokenMapper, cache, client);
    }

    @Bean
    public Cache cache() {
        return new Cache();
    }

    @Bean
    public CommandLineRunner startupTokenReader(
            final Supplier<Token> tokenSupplier,
            final FacebookClient facebookClient,
            final Cache cache
    ) {
        return new StartupTokenReader(tokenSupplier, facebookClient, cache);
    }

    @Bean
    public Supplier<Token> tokenSupplier(
            final TokenRepository tokenRepository
    ) {
        return new TokenSupplier(tokenRepository);
    }
}
