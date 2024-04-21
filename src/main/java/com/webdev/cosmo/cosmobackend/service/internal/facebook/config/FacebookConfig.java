package com.webdev.cosmo.cosmobackend.service.internal.facebook.config;

import com.webdev.cosmo.cosmobackend.service.internal.facebook.mapper.TokenMapper;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.repository.TokenRepository;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.SaveTokenConsumer;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.AccessTokenRefreshJob;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.AccessTokenService;
import com.webdev.cosmo.cosmobackend.service.external.FacebookClient;
import org.openapitools.model.TokenModel;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.function.Consumer;

@Configuration
public class FacebookConfig {

    public static final String ACCESS_TOKEN_REFRESH_JOB = "AccessTokenRefreshJob";
    public static final String ACCESS_TOKEN_REFRESH_TRIGGER = "AccessTokenRefreshTrigger";

    @Bean
    public QuartzJobBean accessTokenRefreshJob(
            final AccessTokenService accessTokenService
    ) {
        return new AccessTokenRefreshJob(accessTokenService);
    }

    @Bean
    public AccessTokenService accessTokenService(
            @Value("${facebook.client-id}") final String clientId,
            @Value("${facebook.client-secret}") final String clientSecret,
            final FacebookClient facebookClient
            ) {
        return new AccessTokenService(clientId, clientSecret, facebookClient);
    }

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
            final TokenMapper tokenMapper
            ) {
        return new SaveTokenConsumer(tokenRepository, tokenMapper);
    }
}
