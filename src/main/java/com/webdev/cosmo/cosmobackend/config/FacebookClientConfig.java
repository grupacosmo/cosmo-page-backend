package com.webdev.cosmo.cosmobackend.config;

import com.webdev.cosmo.cosmobackend.emulator.FacebookClientEmulator;
import com.webdev.cosmo.cosmobackend.service.common.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import com.webdev.cosmo.cosmobackend.util.interfaces.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FacebookClientConfig {

    @Bean
    @ConditionalOnProperty(name = "facebook.emulator.enabled", havingValue = "true")
    public FacebookClient emulatedFacebookClient(Cache cache) {
        return new FacebookClientEmulator(cache);
    }

    @Bean
    @ConditionalOnProperty(name = "facebook.emulator.enabled", havingValue = "false", matchIfMissing = true)
    public FacebookClient facebookClient(ApplicationContext applicationContext) {
        FeignClientFactoryBean factory = new FeignClientFactoryBean();
        factory.setApplicationContext(applicationContext);
        factory.setName("facebookAccessTokenClient");
        factory.setContextId("facebookAccessTokenClient");
        factory.setUrl("https://graph.facebook.com");
        factory.setType(FacebookClient.class);
        return (FacebookClient) factory.getObject();
    }

    @Bean
    @ConditionalOnProperty(name = "facebook.emulator.enabled", havingValue = "true")
    public CommandLineRunner emulatorDataSeed(Executor postsSyncExecutor) {
        return args -> {
            try {
                postsSyncExecutor.execute();
                log.info("Seeded sample posts from the Facebook emulator.");
            } catch (Exception e) {
                log.warn("Could not seed sample posts: {}", e.getMessage());
            }
        };
    }
}