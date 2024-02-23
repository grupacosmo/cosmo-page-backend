package com.webdev.cosmo.cosmobackend.service.external.facebook.config;

import com.webdev.cosmo.cosmobackend.service.external.facebook.async.AccessTokenRefreshJob;
import com.webdev.cosmo.cosmobackend.service.external.facebook.async.AccessTokenService;
import com.webdev.cosmo.cosmobackend.service.external.facebook.external.FacebookAccessTokenClient;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

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
            final FacebookAccessTokenClient facebookAccessTokenClient
            ) {
        return new AccessTokenService(clientId, clientSecret, facebookAccessTokenClient);
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
}
