package com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenRefreshJob extends QuartzJobBean {

    private final Cache cache;

    @Override
    protected void executeInternal(@NonNull JobExecutionContext context) {
        log.info("Executing scheduled process");
        final String token = cache.getPageAccessToken();
        log.info("Ending scheduled process with result token: {}", token);
    }
}
